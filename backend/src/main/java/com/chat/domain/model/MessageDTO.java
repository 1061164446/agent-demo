package com.chat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String role;
    private String content;
    private long timestamp;
    
    public static MessageDTO fromMessage(Message message) {
        return new MessageDTO(
            message.getRole(),
            message.getContent(),
            message.getTimestampMillis()
        );
    }
} 