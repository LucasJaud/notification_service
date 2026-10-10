package com.jaud.lucas.notification_service.message;

import java.io.Serializable;
import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class Message implements Serializable{
    
    private UUID id;

    private String payload;

}
