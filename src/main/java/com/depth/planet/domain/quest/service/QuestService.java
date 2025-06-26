package com.depth.planet.domain.quest.service;

import com.depth.planet.domain.file.entity.EvidenceImage;
import com.depth.planet.domain.file.handler.FileSystemHandler;
import com.depth.planet.domain.file.repository.AttachedFileRepository;
import com.depth.planet.domain.quest.dto.QuestDto;
import com.depth.planet.domain.quest.entity.Quest;
import com.depth.planet.domain.quest.repository.QuestQueryRepository;
import com.depth.planet.domain.quest.repository.QuestRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;
import com.depth.planet.system.security.model.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final FileSystemHandler fileSystemHandler;
    private final AttachedFileRepository attachedFileRepository;
    private final QuestRepository questRepository;
    private final QuestQueryRepository questQueryRepository;

    public List<QuestDto.QuestResponse> findMyQuestsBetween(LocalDate startDate, LocalDate endDate, UserDetails user) {
        List<Quest> result = questQueryRepository.findBetween(startDate, endDate, user);
        return result.stream()
                .map(QuestDto.QuestResponse::from)
                .toList();
    }

    //TODO: 퀘스트 제안 생성

    //TODO: 퀘스트 제안 수락

    public QuestDto.QuestResponse findMyQuestToday(UserDetails user) {
        Quest result = questQueryRepository.findToday(user).orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));
        return QuestDto.QuestResponse.from(result);
    }


    public QuestDto.QuestResponse completeQuest(Long questId, QuestDto.CompleteQuestRequest request, UserDetails user) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        if(quest.getIsCompleted()) {
            throw new RestException(ErrorCode.QUEST_ALREADY_COMPLETED);
        }

        quest.complete();

        if(request.getEvidenceImage() != null) {
            EvidenceImage toSave = EvidenceImage.from(request.getEvidenceImage(), quest);
            fileSystemHandler.saveFile(request.getEvidenceImage(), toSave);
            EvidenceImage saved = attachedFileRepository.save(toSave);
            quest.setEvidenceImage(saved);
        }

        return QuestDto.QuestResponse.from(quest);
    }
}
