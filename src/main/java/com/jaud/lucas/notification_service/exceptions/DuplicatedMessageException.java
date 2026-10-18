package com.jaud.lucas.notification_service.exceptions;

public class DuplicatedMessageException extends RuntimeException{

    public DuplicatedMessageException(String msg){
        super(msg);
    }
    
}
