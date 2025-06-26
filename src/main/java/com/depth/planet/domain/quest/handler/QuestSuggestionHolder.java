package com.depth.planet.domain.quest.handler;

import com.depth.planet.domain.ai.llm.dto.AiQuestDto;
import com.depth.planet.domain.quest.dto.QuestDto;
import com.depth.planet.domain.quest.model.QuestSuggestion;
import com.depth.planet.system.security.model.UserDetails;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@NoArgsConstructor
public class QuestSuggestionHolder {
    private final ExecutorService questSuggestionExpirer = Executors.newScheduledThreadPool(1);
    private final Map<String, QuestSuggestion> questSuggestionMap = new ConcurrentHashMap<>();

    public List<QuestDto.QuestSuggestionResponse> findByUser(UserDetails userDetails) {
        List<QuestDto.QuestSuggestionResponse> suggestions = new ArrayList<>();

        for (Map.Entry<String, QuestSuggestion> entry : questSuggestionMap.entrySet()) {
            if (entry.getValue().getRequesterEmail().equals(userDetails.getKey())) {
                suggestions.add(QuestDto.QuestSuggestionResponse.from(entry.getValue()));
            }
        }

        return suggestions;
    }

    public Optional<QuestDto.QuestSuggestionResponse> findByUuid(String uuid) {
        QuestSuggestion suggestion = questSuggestionMap.get(uuid);

        return Optional.ofNullable(suggestion)
                .map(QuestDto.QuestSuggestionResponse::from);
    }

    public List<QuestDto.QuestSuggestionResponse> registerAiSuggestions(
            List<AiQuestDto.AIQuestSuggestionResponse> aiQuestSuggestionResponses,
            UserDetails userDetails
    ) {
        clearSuggestionsFor(userDetails);

        List<QuestDto.QuestSuggestionResponse> suggestions = new ArrayList<>();

        for (AiQuestDto.AIQuestSuggestionResponse suggestion : aiQuestSuggestionResponses) {
            QuestSuggestion questSuggestion = QuestSuggestion.of(suggestion, userDetails.getUser());
            addQuestSuggestion(questSuggestion);
            suggestions.add(QuestDto.QuestSuggestionResponse.from(questSuggestion));
        }

        return suggestions;
    }

    private void addQuestSuggestion(QuestSuggestion suggestion) {
        questSuggestionMap.put(suggestion.getUuid(), suggestion);
        // Schedule expiration task
        questSuggestionExpirer.submit(() -> {
            try {
                //expire after 10 minutes
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            questSuggestionMap.remove(suggestion.getUuid());
        });
    }

    private void clearSuggestionsFor(UserDetails user) {
        List<String> keysToRemove = new ArrayList<>();

        for (Map.Entry<String, QuestSuggestion> entry : questSuggestionMap.entrySet()) {
            if (entry.getValue().getRequesterEmail().equals(user.getKey())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (String key : keysToRemove) {
            questSuggestionMap.remove(key);
        }
    }
}
