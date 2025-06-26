package com.depth.planet.system.configuration;

import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {
    @Bean(name = "json-model")
    @Qualifier("json-model")
    public GoogleAiGeminiChatModel jsonModel (
            @Value("${gemini.apiKey}") String apiKey
    ) {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .responseFormat(ResponseFormat.JSON)
                .build();
    }

    @Bean(name = "plain-text-model")
    @Qualifier("plain-text-model")
    public GoogleAiGeminiChatModel plainTextModel (
            @Value("${gemini.apiKey}") String apiKey
    ) {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .responseFormat(ResponseFormat.TEXT)
                .build();
    }
}
