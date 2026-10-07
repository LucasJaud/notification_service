package com.jaud.lucas.notification_service.message.dtos;

import java.util.List;

public record RequestBatchMessage(List<MessageRequest> messages) {
    
}
