package com.jaud.lucas.notification_service.exceptions;

public class ProcessingMessageException extends RuntimeException{
    
    public ProcessingMessageException(String msg){
        super(msg);
    }
}
