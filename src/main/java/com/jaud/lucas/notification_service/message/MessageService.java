package com.jaud.lucas.notification_service.message;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaud.lucas.notification_service.exceptions.InvalidPayloadException;
import com.jaud.lucas.notification_service.exceptions.MessageProcessingException;
import com.jaud.lucas.notification_service.message.dtos.RequestBatchMessage;
import com.jaud.lucas.notification_service.message.entities.MessageBatch;
import com.jaud.lucas.notification_service.message.entities.MessageBatchItem;
import com.jaud.lucas.notification_service.message.entities.MessageStatus;
import com.jaud.lucas.notification_service.message.producer.Producer;
import com.jaud.lucas.notification_service.message.repositories.MessageBatchItensRepository;
import com.jaud.lucas.notification_service.message.repositories.MessageBatchRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MessageService {
    
    private final MessageBatchRepository batchRepository;
    private final MessageBatchItensRepository itensRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final Producer producer;

    public MessageService(MessageBatchRepository batchRepository,
            MessageBatchItensRepository itensRepository, 
            @Qualifier("rabbitmq") Producer producer){
        this.batchRepository = batchRepository;
        this.itensRepository = itensRepository;
        this.producer = producer;
    }

    @Transactional 
    public MessageBatch createBatchMessage(RequestBatchMessage request) throws JsonProcessingException{
        MessageBatch batch = new MessageBatch();
        batch.setMessages(new HashSet<>());
        batch.setCreatedAt(Instant.now());

        List<Message> messages = transformMessages(request);
        sendToProducer(messages);

        saveMessages(batch, messages);
        MessageBatch savedBatch = batchRepository.save(batch);

        return savedBatch;
    }

    public MessageBatchItem getItem(UUID uuid){
        try{
            MessageBatchItem msg = itensRepository.getReferenceById(uuid);
            return msg;
        } catch (EntityNotFoundException ex){
            logger.error("message with uuid: {} not found", uuid);
            return null;
        }
    }

    private List<Message> transformMessages(RequestBatchMessage request) throws InvalidPayloadException{
        List<Message> messagesToQueue = new LinkedList<>();
        ObjectMapper mapper = new ObjectMapper();
        request.messages().forEach(msg -> {
            try {
                Message messageToQueue = Message.builder()
                    .id(UUID.randomUUID())
                    .payload(mapper.writeValueAsString(msg.payload()))
                    .build();
                messagesToQueue.add(messageToQueue);
            } catch (JsonProcessingException e) {
                logger.error("Error on message paylod {} caused error: {}",msg.payload(), e.getMessage());
                throw new InvalidPayloadException("Error processing message payload");
            }
        });
        
        return messagesToQueue;
    }

    private void sendToProducer(List<Message> messages){
        try{
            messages.forEach(message -> {
                producer.send(message);
            });
        } catch (Exception ex){
            logger.error("Error sending message to Queue");
            throw new MessageProcessingException("Error sending message to Queue to process");
        }
        
    }

    private void saveMessages(MessageBatch batch, List<Message> messages){
        messages.forEach(msg -> {
            MessageBatchItem msgBatch = new MessageBatchItem();
            msgBatch.setStatus(MessageStatus.PENDING);
            msgBatch.setId(msg.getId());
            batch.addMessage(msgBatch);
            itensRepository.save(msgBatch);
        });
    }
    
}
