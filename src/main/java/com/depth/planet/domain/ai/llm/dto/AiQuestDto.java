package com.depth.planet.domain.ai.llm.dto;

import dev.langchain4j.model.output.structured.Description;
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
    @Description("AI가 제안한 퀘스트 제목과 격려 메시지를 포함하는 응답 DTO")
    public static class AIQuestSuggestionResponse {
        @Description("AI가 제안한 퀘스트 제목")
        @Schema(description = "AI가 제안한 퀘스트 제목", example = "책 읽기 30분")
        private String title;
    }
}
