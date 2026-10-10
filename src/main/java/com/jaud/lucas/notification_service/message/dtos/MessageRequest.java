package com.jaud.lucas.notification_service.message.dtos;

import tools.jackson.databind.JsonNode;

public record MessageRequest(JsonNode payload) {
    

}