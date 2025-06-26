package com.depth.planet.domain.quest.dto;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.depth.planet.domain.file.dto.FileDto;
import com.depth.planet.domain.quest.entity.Quest;
import com.depth.planet.domain.quest.model.QuestSuggestion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class QuestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "퀘스트 응답 DTO")
    public static class QuestResponse {
        @Schema(description = "퀘스트 ID", example = "1")
        private Long id;

        @Schema(description = "퀘스트 제목", example = "30분 산책하기")
        private String title;

        @Schema(description = "격려 메시지", example = "오늘도 건강한 하루를 위해 화이팅!")
        private String encouragement;

        @Schema(description = "완료 여부", example = "false")
        private Boolean isCompleted;

        @Schema(description = "완료 일시", example = "2024-01-15T14:30:00")
        private LocalDateTime completedAt;

        @Schema(description = "생성 일시", example = "2024-01-15T09:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "최종 수정 일시", example = "2024-01-15T14:30:00")
        private LocalDateTime lastModifiedAt;

        @Schema(description = "증거 이미지 정보")
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
    @Schema(description = "퀘스트 제안 응답 DTO")
    public static class QuestSuggestionResponse {
        @Schema(description = "퀘스트 제안 고유 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        private String uuid;

        @Schema(description = "제안된 퀘스트 제목", example = "책 한 챕터 읽기")
        private String title;

        @Schema(description = "격려 메시지", example = "새로운 지식을 얻는 즐거운 시간을 가져보세요!")
        private String encouragement;

        @Schema(description = "제안 생성 일시", example = "2024-01-15T09:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "제안 요청자 이메일", example = "user@example.com")
        private String requesterEmail;

        public static QuestSuggestionResponse from(QuestSuggestion questSuggestion) {
            return QuestSuggestionResponse.builder()
                    .uuid(questSuggestion.getUuid())
                    .title(questSuggestion.getTitle())
                    .encouragement(questSuggestion.getEncouragement())
                    .createdAt(questSuggestion.getCreatedAt())
                    .requesterEmail(questSuggestion.getRequesterEmail())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "퀘스트 완료 요청 DTO")
    public static class CompleteQuestRequest {
        @Nullable
        @Schema(description = "퀘스트 완료 증거 이미지 파일", type = "string", format = "binary")
        private MultipartFile evidenceImage;
    }
}
