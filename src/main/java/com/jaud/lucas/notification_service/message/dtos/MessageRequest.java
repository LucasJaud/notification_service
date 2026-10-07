package com.jaud.lucas.notification_service.message.dtos;

import com.fasterxml.jackson.databind.JsonNode;

public record MessageRequest(JsonNode payload) {
    

}