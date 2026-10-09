package com.jaud.lucas.notification_service.exceptions;

public class InvalidPayloadException extends RuntimeException{
    
    public InvalidPayloadException(String msg){
        super(msg);
    }
}
