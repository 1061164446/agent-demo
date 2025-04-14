package com.chat.infrastructure.service.impl;

import com.chat.domain.model.Conversation;
import com.chat.domain.model.Message;
import com.chat.domain.service.ChatDomainService;
import com.chat.infrastructure.service.AIChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天领域服务实现
 */
@Slf4j
@Service
public class ChatDomainServiceImpl implements ChatDomainService {
    private final Conversation conversation;
    private final AIChatService aiChatService;

    @Autowired
    public ChatDomainServiceImpl(AIChatService aiChatService) {
        this.conversation = new Conversation(5); // 最大历史记录数
        this.aiChatService = aiChatService;
    }

    @Override
    public Message processUserMessage(Message userMessage) {
        log.info("领域服务开始处理消息: {}", userMessage);
        
        // 添加用户消息到对话
        conversation.addMessage(userMessage);
        log.info("用户消息已添加到对话");
        
        // 获取AI回复
        String aiResponse = aiChatService.sendMessage(userMessage.getContent());
        log.info("获取到AI回复: {}", aiResponse);
        
        Message assistantMessage = new Message("assistant", aiResponse);
        log.info("创建助手消息: {}", assistantMessage);
        
        // 添加AI回复到对话
        conversation.addMessage(assistantMessage);
        log.info("助手消息已添加到对话");
        
        return assistantMessage;
    }

    @Override
    public List<Message> getConversationHistory() {
        return conversation.getRecentHistory();
    }

    @Override
    public void clearConversationHistory() {
        conversation.clearHistory();
    }
} 