package com.chat.infrastructure.controller;

import com.chat.application.service.ChatApplicationService;
import com.chat.interfaces.model.ApiResponse;
import com.chat.interfaces.model.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 聊天控制器
 * 处理HTTP请求
 */
@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatApplicationService chatApplicationService;

    @Autowired
    public ChatController(ChatApplicationService chatApplicationService) {
        this.chatApplicationService = chatApplicationService;
    }

    @PostMapping("/send")
    public ApiResponse<MessageDTO> sendMessage(@RequestBody Map<String, String> request) {
        try {
            log.info("收到发送消息请求: {}", request);
            
            String content = request.get("content");
            if (content == null || content.trim().isEmpty()) {
                log.warn("请求内容为空");
                throw new IllegalArgumentException("消息内容不能为空");
            }
            
            log.info("开始处理消息: {}", content);
            MessageDTO response = chatApplicationService.sendMessage(content);
            log.info("消息处理完成，响应: {}", response);
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("处理消息请求失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ApiResponse<List<MessageDTO>> getChatHistory() {
        try {
            log.info("收到获取聊天历史请求");
            List<MessageDTO> history = chatApplicationService.getChatHistory();
            log.info("返回聊天历史记录数量: {}", history.size());
            return ApiResponse.success(history);
        } catch (Exception e) {
            log.error("获取聊天历史失败", e);
            throw e;
        }
    }

    @DeleteMapping("/history")
    public ApiResponse<Void> clearChatHistory() {
        try {
            log.info("收到清空聊天历史请求");
            chatApplicationService.clearChatHistory();
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("清空聊天历史失败", e);
            throw e;
        }
    }
} 