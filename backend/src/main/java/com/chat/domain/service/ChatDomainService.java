package com.chat.domain.service;

import com.chat.domain.model.Message;

/**
 * 聊天领域服务
 * 定义核心业务能力
 */
public interface ChatDomainService {
    /**
     * 处理用户消息并获取AI回复
     * @param userMessage 用户消息
     * @return AI回复消息
     */
    Message processUserMessage(Message userMessage);
    
    /**
     * 获取对话历史
     * @return 对话历史列表
     */
    java.util.List<Message> getConversationHistory();
    
    /**
     * 清空对话历史
     */
    void clearConversationHistory();
} 