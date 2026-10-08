package com.jaud.lucas.notification_service.queue.consumer;

import com.jaud.lucas.notification_service.message.Message;

public interface MessageConsumer {
    void consume(Message message);
}
