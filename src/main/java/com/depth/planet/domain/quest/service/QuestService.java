package com.depth.planet.domain.quest.service;

import com.depth.planet.domain.file.entity.EvidenceImage;
import com.depth.planet.domain.file.handler.FileSystemHandler;
import com.depth.planet.domain.file.repository.AttachedFileRepository;
import com.depth.planet.domain.quest.dto.QuestDto;
import com.depth.planet.domain.quest.entity.Quest;
import com.depth.planet.domain.quest.repository.QuestRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;
import com.depth.planet.system.security.model.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final FileSystemHandler fileSystemHandler;
    private final AttachedFileRepository attachedFileRepository;
    private QuestRepository questRepository;

    public List<Quest> findMyQuestsBetween(LocalDate startDate, LocalDate endDate, UserDetails user) {
        // 특정 기간 동안의 퀘스트 조회 (이미 했던)
        //TODO: not yet implemented
        return null;
    }

    //TODO: 퀘스트 제안 생성

    //TODO: 퀘스트 제안 수락

    public Quest findMyQuestToday(UserDetails user) {
        // 오늘의 퀘스트 조회 (오늘 할)
        return null; // TODO: not yet implemented
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
