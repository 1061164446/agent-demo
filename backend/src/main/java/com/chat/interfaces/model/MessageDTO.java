package com.chat.interfaces.model;

import com.chat.domain.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 消息数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String role;
    private String content;
    private long timestamp;

    /**
     * 从领域模型转换为DTO
     * @param message 领域模型
     * @return DTO对象
     */
    public static MessageDTO fromMessage(Message message) {
        return new MessageDTO(
            message.getRole(),
            message.getContent(),
            Instant.now().toEpochMilli()
        );
    }
} 