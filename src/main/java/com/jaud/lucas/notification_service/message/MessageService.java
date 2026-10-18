package com.jaud.lucas.notification_service.message;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.jaud.lucas.notification_service.exceptions.DuplicatedMessageException;
import com.jaud.lucas.notification_service.exceptions.HttpRequestException;
import com.jaud.lucas.notification_service.exceptions.InvalidPayloadException;
import com.jaud.lucas.notification_service.exceptions.MessageProcessingException;
import com.jaud.lucas.notification_service.message.dtos.MessageBatchDTO;
import com.jaud.lucas.notification_service.message.dtos.RequestBatchMessage;
import com.jaud.lucas.notification_service.message.entities.MessageBatch;
import com.jaud.lucas.notification_service.message.entities.MessageBatchItem;
import com.jaud.lucas.notification_service.message.entities.MessageStatus;
import com.jaud.lucas.notification_service.message.entities.ProcessedMessage;
import com.jaud.lucas.notification_service.message.producer.Producer;
import com.jaud.lucas.notification_service.message.repositories.MessageBatchItensRepository;
import com.jaud.lucas.notification_service.message.repositories.MessageBatchRepository;
import com.jaud.lucas.notification_service.message.repositories.ProcessedMessageRepository;
import com.jaud.lucas.notification_service.rest.CustomRestClient;

import jakarta.persistence.EntityNotFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class MessageService {
    
    private final MessageBatchRepository batchRepository;
    private final MessageBatchItensRepository itensRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final Producer producer;
    private final ProcessedMessageRepository processedMessageRepository;

    public MessageService(MessageBatchRepository batchRepository,
            MessageBatchItensRepository itensRepository, 
            @Qualifier("rabbitmq") Producer producer,
            ProcessedMessageRepository processedMessageRepository){
        this.batchRepository = batchRepository;
        this.itensRepository = itensRepository;
        this.producer = producer;
        this.processedMessageRepository = processedMessageRepository;
    }

    @Transactional 
    public MessageBatchDTO createBatchMessage(RequestBatchMessage request){
        MessageBatch batch = new MessageBatch();
        batch.setMessages(new HashSet<>());
        batch.setCreatedAt(Instant.now());
        MessageBatch savedBatch = batchRepository.save(batch);

        List<Message> messages = generateMessages(request);
        sendToProducer(messages);
        saveMessages(batch, messages);

        return MessageBatchDTO.generateMessageBatch(savedBatch);
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

    private List<Message> generateMessages(RequestBatchMessage request) throws InvalidPayloadException{
        List<Message> messagesToQueue = new LinkedList<>();
        ObjectMapper mapper = new ObjectMapper();
        request.messages().forEach(msg -> {
            try {
                Message messageToQueue = Message.builder()
                    .id(UUID.randomUUID())
                    .payload(msg.payload())
                    .urlString(msg.url())
                    .build();
                messagesToQueue.add(messageToQueue);
            } catch (JacksonException e) {
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
            msgBatch.setBatch(batch);
            batch.addMessage(msgBatch);
            itensRepository.save(msgBatch);
        });
    }

    public void processMessage(Message message) throws IllegalArgumentException {
        CustomRestClient client = new CustomRestClient();
        ResponseEntity<Void> response = client.sendRequest(message);
        if(!response.getStatusCode().is2xxSuccessful()){
            logger.error(""); 
            throw new HttpClientErrorException(response.getStatusCode());
        }
    }

    public void checkProcessedMessage(Message message) throws DuplicatedMessageException{
        ProcessedMessage processedMessage = ProcessedMessage
            .builder()
            .id(message.getId())
            .processedAt(Instant.now())
            .build();
        try{
            processedMessageRepository.saveAndFlush(processedMessage);
        } catch (DataIntegrityViolationException ex){
            throw new DuplicatedMessageException("message was already processed");
        }   
    }

    @Transactional 
    public void updateBatchItemStatus(Message message, MessageStatus status ){
        itensRepository.updateBatchItemStatus(message.getId(), status);
    }
    
}
