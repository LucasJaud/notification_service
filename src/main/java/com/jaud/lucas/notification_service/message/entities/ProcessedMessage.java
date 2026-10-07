package com.jaud.lucas.notification_service.message.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "tb_processed_message")
@Data 
@NoArgsConstructor 
public class ProcessedMessage {

    @Id 
    private UUID id;
    
    @Column(name = "processed_at")
    private Instant processedAt;

}
