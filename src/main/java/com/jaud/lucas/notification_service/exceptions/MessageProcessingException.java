package com.jaud.lucas.notification_service.exceptions;

public class MessageProcessingException extends RuntimeException{
    
    public MessageProcessingException(String msg){
        super(msg);
    }
}
