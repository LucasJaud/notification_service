package com.jaud.lucas.notification_service.configuration;



import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.rabbitmq.client.AMQP.Exchange;

@Configuration 
public class RabbitmqConfig {

    @Bean 
    public JacksonJsonMessageConverter jacksonJsonMessageConverter(){
        return  new JacksonJsonMessageConverter();
    }
    
}
