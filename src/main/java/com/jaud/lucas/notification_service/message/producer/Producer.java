package com.jaud.lucas.notification_service.message.producer;

import com.jaud.lucas.notification_service.message.Message;

public interface Producer {
    public void send(Message msg);
}
