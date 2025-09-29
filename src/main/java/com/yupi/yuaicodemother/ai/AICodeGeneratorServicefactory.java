package com.yupi.yuaicodemother.ai;


import com.yupi.yuaicodemother.ai.service.AiCodeGeneratorService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AICodeGeneratorServicefactory {

    @Resource
    private ChatModel chatmodel;

    @Resource
    private StreamingChatModel streamingChatModel;

    /**
     * 创建一个AiCodeGeneratorService实例
     * @return
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return AiServices.builder(AiCodeGeneratorService.class)
                .chatModel(chatmodel)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}
