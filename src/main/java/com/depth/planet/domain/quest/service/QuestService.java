package com.depth.planet.domain.quest.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depth.planet.domain.ai.llm.dto.AiQuestDto;
import com.depth.planet.domain.ai.llm.service.EncouragementGenerateService;
import com.depth.planet.domain.ai.llm.service.QuestFeedbackGenerateService;
import com.depth.planet.domain.ai.llm.service.QuestGenerateService;
import com.depth.planet.domain.file.entity.EvidenceImage;
import com.depth.planet.domain.file.handler.FileSystemHandler;
import com.depth.planet.domain.file.repository.AttachedFileRepository;
import com.depth.planet.domain.quest.dto.QuestDto;
import com.depth.planet.domain.quest.entity.Quest;
import com.depth.planet.domain.quest.handler.QuestSuggestionHolder;
import com.depth.planet.domain.quest.repository.QuestQueryRepository;
import com.depth.planet.domain.quest.repository.QuestRepository;
import com.depth.planet.domain.user.entity.User;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;
import com.depth.planet.system.security.model.UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestService {
    private final FileSystemHandler fileSystemHandler;
    private final AttachedFileRepository attachedFileRepository;
    private final QuestRepository questRepository;
    private final QuestQueryRepository questQueryRepository;
    private final QuestSuggestionHolder questSuggestionHolder;
    private final QuestGenerateService questGenerateService;
    private final QuestFeedbackGenerateService questFeedbackGenerateService;
    private final EncouragementGenerateService encouragementGenerateService;
    private final UserTierService userTierService;

    public List<QuestDto.QuestResponse> findMyQuestsBetween(LocalDate startDate, LocalDate endDate, UserDetails user) {
        List<Quest> result = questQueryRepository.findBetween(startDate, endDate, user);
        return result.stream()
                .map(QuestDto.QuestResponse::from)
                .toList();
    }

    public List<QuestDto.QuestSuggestionResponse> generateQuestSuggestions(UserDetails user) {
        User currentUser = user.getUser();
        // 취미를 ,를 구분자로 나누어 문자열로 변환
        String hobbies = currentUser.getHobbies().stream()
                .reduce((hobby1, hobby2) -> hobby1 + "," + hobby2)
                .orElse("");

        List<AiQuestDto.AIQuestSuggestionResponse> generatedAiSuggestions = questGenerateService
                .generateQuestSuggestions(
                        currentUser.getMbti().name(),
                        currentUser.getGender().name(),
                        hobbies,
                        currentUser.getAgeGroup().name());

        if (generatedAiSuggestions.isEmpty()) {
            throw new RestException(ErrorCode.QUEST_SUGGESTION_NOT_FOUND);
        }

        if (generatedAiSuggestions.size() != 4) {
            throw new RestException(ErrorCode.QUEST_SUGGESTION_SIZE_MISMATCH);
        }

        return questSuggestionHolder.registerAiSuggestions(generatedAiSuggestions, user);
    }

    public List<QuestDto.QuestSuggestionResponse> findCachedQuestSuggestions(UserDetails user) {
        List<QuestDto.QuestSuggestionResponse> suggestions = questSuggestionHolder.findByUser(user);

        if (suggestions.isEmpty()) {
            throw new RestException(ErrorCode.QUEST_SUGGESTION_NOT_FOUND);
        }

        return suggestions;
    }

    public QuestDto.QuestResponse approveQuestSuggestion(String uuid, UserDetails user) {
        // 오늘 이미 승인한 퀘스트가 있는지 확인
        Optional<Quest> todayQuest = questQueryRepository.findToday(user);
        if (todayQuest.isPresent()) {
            throw new RestException(ErrorCode.QUEST_ALREADY_APPROVED_TODAY);
        }

        Optional<QuestDto.QuestSuggestionResponse> suggestionOpt = questSuggestionHolder.findByUuid(uuid);
        if (suggestionOpt.isEmpty()) {
            throw new RestException(ErrorCode.QUEST_SUGGESTION_NOT_FOUND);
        }

        QuestDto.QuestSuggestionResponse suggestion = suggestionOpt.get();
        AiQuestDto.AIQuestEncouragementResponse response = encouragementGenerateService
                .generateEncouragement(suggestion.getTitle());

        Quest quest = Quest.of(suggestion, user.getUser(), response.getEncouragement());
        Quest savedQuest = questRepository.save(quest);

        return QuestDto.QuestResponse.from(savedQuest);
    }

    public QuestDto.QuestResponse findMyQuestToday(UserDetails user) {
        Quest result = questQueryRepository.findToday(user)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));
        return QuestDto.QuestResponse.from(result);
    }

    public QuestDto.QuestResponse completeQuest(Long questId, QuestDto.CompleteQuestRequest request, UserDetails user) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        if (quest.getIsCompleted()) {
            throw new RestException(ErrorCode.QUEST_ALREADY_COMPLETED);
        }

        quest.complete();

        // 증거 이미지 처리 및 경험치 계산
        boolean hasEvidenceImage = false;
        if (request.getEvidenceImage() != null) {
            EvidenceImage toSave = EvidenceImage.from(request.getEvidenceImage(), quest);
            fileSystemHandler.saveFile(request.getEvidenceImage(), toSave);
            EvidenceImage saved = attachedFileRepository.save(toSave);
            quest.setEvidenceImage(saved);
            hasEvidenceImage = true;
        }

        String feedback = questFeedbackGenerateService.generateQuestFeedback(quest);
        quest.setFeedback(feedback);

        // 경험치 추가: 사진 업로드 시 +30, 미업로드 시 +20
        int expToAdd = hasEvidenceImage ? 30 : 20;
        userTierService.addExpToUser(user.getUser().getEmail(), expToAdd);

        return QuestDto.QuestResponse.from(quest);
    }

    public void deleteAllQuestsByUser(UserDetails user) {
        // 해당 유저의 모든 퀘스트 조회 (evidenceImage join)
        List<Quest> userQuests = questQueryRepository.findAllByUser(user);

        if (userQuests.isEmpty()) {
            return;
        }

        // 각 퀘스트의 증거 이미지 삭제 (실제 파일 시스템에서)
        for (Quest quest : userQuests) {
            if (quest.getEvidenceImage() != null) {
                try {
                    fileSystemHandler.deleteFile(quest.getEvidenceImage());
                } catch (Exception e) {
                    // 파일 삭제 실패해도 DB 삭제는 진행
                    // 로그 기록 (필요시 logger 추가)
                }
            }
        }

        // 퀘스트 삭제 (cascade로 연관 엔티티들도 삭제됨)
        questRepository.deleteAll(userQuests);
    }
}
