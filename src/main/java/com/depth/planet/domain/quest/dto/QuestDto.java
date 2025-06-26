package com.depth.planet.domain.quest.dto;

import com.depth.planet.domain.quest.entity.Quest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class QuestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class QuestResponse {
        private Long id;
        private String title;
        private String encouragement;

        public static QuestResponse from(Quest quest) {
            return QuestResponse.builder()
                    .id(quest.getId())
                    .title(quest.getTitle())
                    .encouragement(quest.getEncouragement())
                    .build();
        }
    }
}
