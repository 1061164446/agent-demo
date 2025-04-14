package com.chat.domain.model;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * 对话聚合根
 * 包含对话的核心业务逻辑和规则
 */
@Getter
public class Conversation {
    private final List<Message> messages;
    private final int maxHistorySize;
    
    public Conversation(int maxHistorySize) {
        this.messages = new ArrayList<>();
        this.maxHistorySize = maxHistorySize;
    }
    
    /**
     * 添加消息到对话
     * @param message 要添加的消息
     */
    public void addMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("消息不能为空");
        }
        
        messages.add(message);
        maintainHistorySize();
    }
    
    /**
     * 维护历史消息大小
     */
    private void maintainHistorySize() {
        while (messages.size() > maxHistorySize * 2) {
            messages.remove(0);
            messages.remove(0);
        }
    }
    
    /**
     * 获取最近的对话历史
     * @return 对话历史列表
     */
    public List<Message> getRecentHistory() {
        return new ArrayList<>(messages);
    }
    
    /**
     * 清空对话历史
     */
    public void clearHistory() {
        messages.clear();
    }
} 