package com.jaud.lucas.notification_service.message.dtos;

import java.time.Instant;
import java.util.List;

import com.jaud.lucas.notification_service.message.entities.MessageBatch;


public record MessageBatchDTO(Integer id, List<MessageDTO> messages, Instant createdAt) {
    
    public static MessageBatchDTO generateMessageBatch(MessageBatch batch){
        
        List<MessageDTO> messages = MessageDTO.generateMessageDTO(batch.getMessages());
        
        return new MessageBatchDTO(batch.getId(), messages, batch.getCreatedAt());
    }
}
