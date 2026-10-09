package com.jaud.lucas.notification_service.message.producer;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jaud.lucas.notification_service.message.Message;

@Service("rabbitmq")
public class RabbitmqProduce implements Producer {

    private final RabbitMessagingTemplate rabbitTemplate;
    
    @Value("${spring.rabbitmq.exchange}")
    private  String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private  String routingKey;


    public RabbitmqProduce(RabbitMessagingTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(Message msg) {
        rabbitTemplate.convertAndSend(exchange, routingKey, msg);
    }
    
}
