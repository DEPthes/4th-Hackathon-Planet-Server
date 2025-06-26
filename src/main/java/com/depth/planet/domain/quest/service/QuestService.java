package com.depth.planet.domain.quest.service;

import com.depth.planet.domain.quest.entity.Quest;
import com.depth.planet.domain.quest.repository.QuestRepository;
import com.depth.planet.system.security.model.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestService {
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

    //TODO: 퀘스트 완료 처리
}
