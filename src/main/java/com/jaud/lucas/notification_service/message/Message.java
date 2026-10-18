package com.jaud.lucas.notification_service.message;

import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class Message{
    
    private UUID id;

    private JsonNode payload;

    private String urlString;
}
