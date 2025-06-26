package com.depth.planet.domain.quest.dto;

import com.depth.planet.domain.file.dto.FileDto;
import com.depth.planet.domain.quest.entity.Quest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class QuestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class QuestResponse {
        private Long id;
        private String title;
        private String encouragement;
        private Boolean isCompleted;
        private LocalDateTime completedAt;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private FileDto.FileResponse evidenceImage;

        public static QuestResponse from(Quest quest) {
            return QuestResponse.builder()
                    .id(quest.getId())
                    .title(quest.getTitle())
                    .encouragement(quest.getEncouragement())
                    .isCompleted(quest.getIsCompleted())
                    .completedAt(quest.getCompletedAt())
                    .createdAt(quest.getCreatedAt())
                    .lastModifiedAt(quest.getLastModifiedAt())
                    .evidenceImage(FileDto.FileResponse.from(quest.getEvidenceImage()))
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class CompleteQuestRequest {
        @Nullable
        private MultipartFile evidenceImage;
    }
}
