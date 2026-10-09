package com.jaud.lucas.notification_service.configuration;



import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.AMQP.Exchange;


public class RabbitmqConfig {

    // @Value("${spring.rabbitmq.template.routing-key}")
    // private String routingKey;

    // @Bean
    // Exchange exchange(){
    //     return new DirectExchange("");
    // }

    // @Bean 
    // Queue queue(){
    //     return new Queue("", false) {
            
    //     };
    // }
    
}
