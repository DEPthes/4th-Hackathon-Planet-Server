package com.depth.planet.domain.quest.model;

import com.depth.planet.domain.ai.llm.dto.AiQuestDto;
import com.depth.planet.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestSuggestion {
    private String uuid;
    private String title;
    private String encouragement;

    private LocalDateTime createdAt;
    private String requesterEmail;

    public static QuestSuggestion of(AiQuestDto.AIQuestSuggestionResponse suggestion, User requester) {
        return QuestSuggestion.builder()
                .uuid(java.util.UUID.randomUUID().toString())
                .title(suggestion.getTitle())
                .encouragement(suggestion.getEncouragement())
                .createdAt(LocalDateTime.now())
                .requesterEmail(requester.getEmail())
                .build();
    }
}
