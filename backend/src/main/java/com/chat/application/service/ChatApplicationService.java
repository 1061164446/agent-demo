package com.chat.application.service;

import com.chat.domain.model.Message;
import com.chat.domain.service.ChatDomainService;
import com.chat.interfaces.model.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天应用服务
 * 处理业务流程和用例
 */
@Slf4j
@Service
public class ChatApplicationService {
    private final ChatDomainService chatDomainService;

    @Autowired
    public ChatApplicationService(ChatDomainService chatDomainService) {
        this.chatDomainService = chatDomainService;
    }

    /**
     * 发送消息并获取回复
     * @param content 消息内容
     * @return 回复消息DTO
     */
    public MessageDTO sendMessage(String content) {
        log.info("应用服务开始处理消息: {}", content);
        
        Message userMessage = new Message("user", content);
        log.info("创建用户消息: {}", userMessage);
        
        Message response = chatDomainService.processUserMessage(userMessage);
        log.info("获取AI回复: {}", response);
        
        MessageDTO dto = MessageDTO.fromMessage(response);
        log.info("转换为DTO: {}", dto);
        
        return dto;
    }

    /**
     * 获取对话历史
     * @return 对话历史DTO列表
     */
    public List<MessageDTO> getChatHistory() {
        log.info("获取对话历史");
        return chatDomainService.getConversationHistory().stream()
                .map(MessageDTO::fromMessage)
                .collect(Collectors.toList());
    }

    /**
     * 清空对话历史
     */
    public void clearChatHistory() {
        log.info("清空对话历史");
        chatDomainService.clearConversationHistory();
    }
} 