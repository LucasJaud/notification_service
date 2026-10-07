package com.jaud.lucas.notification_service.message;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor 
public class Message implements Serializable{
    
    private UUID id;

    private String payload;

}
