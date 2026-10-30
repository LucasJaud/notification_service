package com.jaud.lucas.notification_service.message.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.jaud.lucas.notification_service.exceptions.DuplicatedMessageException;
import com.jaud.lucas.notification_service.exceptions.ProcessingMessageException;
import com.jaud.lucas.notification_service.message.Message;
import com.jaud.lucas.notification_service.message.MessageService;
import com.jaud.lucas.notification_service.message.entities.MessageStatus;
import com.rabbitmq.client.Channel;

@Service("rabbitmqConsumer")
public class RabbitmqConsumer implements Consumer{

    private final MessageService messageService;
    private Logger logger = LoggerFactory.getLogger(RabbitmqConsumer.class);

    public RabbitmqConsumer(MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    public void receive(Message message) {
        messageService.processMessage(message);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue-name}")
    public void receiveAndAcknowledge(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException, ProcessingMessageException {
        
        MessageStatus status = MessageStatus.PROCESSED;
        try {
            messageService.checkProcessedMessageExists(message);
            receive(message);
            messageService.saveProcessedMessage(message);
            ack(channel, tag);
        } catch(IllegalArgumentException ex) {
            nack(channel, tag, false);
            logger.error("Ilegall url argument at {}",message.getUrlString());
            status = MessageStatus.FAILED;
        } catch(HttpClientErrorException | HttpServerErrorException ex) {
            nack(channel, tag, true);
            logger.error("Error sending request to {}, error: {}", message.getUrlString(), ex);
            status = MessageStatus.FAILED;
        } catch (DuplicatedMessageException ex){
            status = MessageStatus.PROCESSED;
            ack(channel, tag);
        } catch (Exception ex){
            nack(channel, tag, true);
            logger.error("Unexpected error: {}", ex);
            status = MessageStatus.FAILED;
        } finally {
            messageService.updateBatchItemStatus(message, status);
        }
    }

    private void ack(Channel channel, long tag) {
        try {
            channel.basicAck(tag, false);
        } catch (IOException ex) {
            logger.error("Error sending ack, tag={}", tag, ex);
        }
    }

    private void nack(Channel channel, long tag, boolean requeue) {
        try {
            channel.basicNack(tag, false, requeue);
        } catch (IOException ex) {
            logger.error("Error sending nack , tag={}", tag, ex);
        }
    }
    
}
