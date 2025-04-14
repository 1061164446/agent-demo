package com.chat.domain.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class Message {
    private String role;
    private String content;
    private LocalDateTime timestamp;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
    
    // 添加获取时间戳的方法，前端需要毫秒级时间戳
    public long getTimestampMillis() {
        return timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
} 