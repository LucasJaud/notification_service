package com.jaud.lucas.notification_service.message.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.jaud.lucas.notification_service.message.Message;
import com.jaud.lucas.notification_service.message.MessageService;

public class RabbitmqConsumer implements Consumer{

    private MessageService messageService;

    public RabbitmqConsumer(MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    // @RabbitListener() 
    public void receive(Message message) {
    
    }
    
}
