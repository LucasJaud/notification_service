package com.jaud.lucas.notification_service.message.dtos;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.jaud.lucas.notification_service.message.entities.MessageBatchItem;
import com.jaud.lucas.notification_service.message.entities.MessageStatus;

public record MessageDTO(UUID id, Instant processedAt, MessageStatus status) {
    
    public static List<MessageDTO> generateMessageDTO(Set<MessageBatchItem> itens){
         

        List<MessageDTO> convertedMessages = itens.stream()
            .map(item -> new MessageDTO(item.getId(), item.getProcessedAt(), item.getStatus()))
            .toList();

        return convertedMessages;
    }
}
