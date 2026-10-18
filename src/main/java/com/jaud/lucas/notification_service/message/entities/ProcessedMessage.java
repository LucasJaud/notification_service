package com.jaud.lucas.notification_service.message.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "tb_processed_message")
@Getter 
@Setter  
@AllArgsConstructor 
@NoArgsConstructor
@Builder 
public class ProcessedMessage {

    @Id 
    private UUID id;
    
    @Column(name = "processed_at")
    private Instant processedAt;

}
