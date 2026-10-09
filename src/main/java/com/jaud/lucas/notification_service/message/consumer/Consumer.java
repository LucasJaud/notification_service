package com.jaud.lucas.notification_service.message.consumer;

import com.jaud.lucas.notification_service.message.Message;

public interface Consumer {

    public void receive(Message message);
    
}
