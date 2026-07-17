package com.example.rag_document_qa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
@Configuration
public class ChatModelConfig {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.model.name}")
    private String modelName;

    @Bean
    public ChatModel chatModel() {

        return OpenAiChatModel.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.3)
                .build();
    }
}
