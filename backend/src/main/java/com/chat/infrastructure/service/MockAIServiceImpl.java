package com.chat.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 模拟AI服务实现
 * 仅用于开发和测试环境，不调用真实API
 */
@Slf4j
@Service
public class MockAIServiceImpl implements AIChatService {

    @Override
    public String sendMessage(String content) {
        log.info("使用本地模拟响应，用户消息: {}", content);
        
        if (content.contains("你好") || content.contains("hello")) {
            return "你好！我是模拟AI助手，很高兴为您服务。";
        } else if (content.contains("天气")) {
            return "今天天气晴朗，温度25°C，非常适合户外活动。";
        } else if (content.contains("介绍") || content.contains("自己")) {
            return "我是一个模拟AI助手，用于开发测试。我不会产生API调用费用。";
        } else if (content.contains("时间") || content.contains("日期")) {
            return "当前时间是模拟时间，真实时间请查看您的设备。";
        } else if (content.contains("API") || content.contains("OpenAI")) {
            return "我是一个模拟实现，不使用真实的AI API。这种方式适合开发环境测试使用。";
        } else {
            return "我理解了您的问题「" + content + "」。作为模拟AI助手，我会尽力提供帮助。";
        }
    }
} 