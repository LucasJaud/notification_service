package com.jaud.lucas.notification_service.queue.producer;

import com.jaud.lucas.notification_service.message.Message;

public interface MessageQueue {
    void send(Message message);

    void acknowledge(Message message);
}
