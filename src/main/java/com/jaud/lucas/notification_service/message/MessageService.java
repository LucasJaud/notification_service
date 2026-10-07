package com.jaud.lucas.notification_service.message;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaud.lucas.notification_service.message.dtos.RequestBatchMessage;
import com.jaud.lucas.notification_service.message.entities.MessageBatch;
import com.jaud.lucas.notification_service.message.entities.MessageBatchItem;
import com.jaud.lucas.notification_service.message.entities.MessageStatus;
import com.jaud.lucas.notification_service.message.repositories.MessageBatchItensRepository;
import com.jaud.lucas.notification_service.message.repositories.MessageBatchRepository;

@Service
public class MessageService {
    
    private final MessageBatchRepository batchRepository;
    private final MessageBatchItensRepository itensRepository;

    public MessageService(MessageBatchRepository batchRepository, MessageBatchItensRepository itensRepository){
        this.batchRepository = batchRepository;
        this.itensRepository = itensRepository;
    }

    @Transactional 
    public Integer createBatchMessage(RequestBatchMessage request){
        MessageBatch batch = new MessageBatch();
        batch.setMessages(new HashSet<>());
        batch.setCreatedAt(Instant.now());

        request.messages().forEach(msg -> {
            MessageBatchItem msgBatch = new MessageBatchItem();
            msgBatch.setStatus(MessageStatus.PENDING);
            msgBatch.setId(UUID.randomUUID());
            batch.addMessage(msgBatch);
            itensRepository.save(msgBatch);
        });

        MessageBatch savedBatch = batchRepository.save(batch);

        return savedBatch.getId();
    }

    
    
}
