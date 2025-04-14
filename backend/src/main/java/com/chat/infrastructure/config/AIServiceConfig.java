package com.chat.infrastructure.config;

import com.chat.infrastructure.service.AIChatService;
import com.chat.infrastructure.service.MockAIServiceImpl;
import com.chat.infrastructure.service.TongyiServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * AI服务配置类
 * 根据配置选择使用哪一种实现
 */
@Configuration
public class AIServiceConfig {

    private final Environment environment;
    private final MockAIServiceImpl mockAIService;
    private final TongyiServiceImpl tongyiService;

    public AIServiceConfig(Environment environment, 
                          MockAIServiceImpl mockAIService, 
                          TongyiServiceImpl tongyiService) {
        this.environment = environment;
        this.mockAIService = mockAIService;
        this.tongyiService = tongyiService;
    }

    /**
     * 根据配置选择使用哪个AI服务实现
     * 如果配置了use-mock=true，则使用模拟服务
     * 否则使用通义千问服务
     */
    @Bean
    public AIChatService aiChatService() {
        String useMock = environment.getProperty("ai.use-mock", "false");
        
        if (Boolean.parseBoolean(useMock)) {
            return mockAIService;
        } else {
            return tongyiService;
        }
    }
} 