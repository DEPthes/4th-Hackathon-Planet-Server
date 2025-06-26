package com.depth.planet.domain.ai.llm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AiQuestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "AI 퀘스트 제안 응답 DTO")
    public static class AIQuestSuggestionResponse {
        @Schema(description = "AI가 제안한 퀘스트 제목", example = "책 읽기 30분")
        private String title;

        @Schema(description = "AI가 제안한 격려 메시지", example = "새로운 지식을 쌓는 즐거운 시간을 가져보세요!")
        private String encouragement;
    }
}
