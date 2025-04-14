package com.chat.infrastructure.service;

/**
 * AI聊天服务接口
 * 定义AI对话功能，允许不同实现切换（OpenAI、模拟数据等）
 */
public interface AIChatService {
    /**
     * 发送消息并获取AI回复
     * @param content 用户消息内容
     * @return AI回复内容
     */
    String sendMessage(String content);
} 