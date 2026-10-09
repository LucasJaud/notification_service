package com.jaud.lucas.notification_service.message.entities;

import java.time.Instant;
import java.util.UUID;

import com.jaud.lucas.notification_service.message.Message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "tb_message_Batch_itens")
@Data
@NoArgsConstructor 
public class MessageBatchItem {
    
    @Id 
    private UUID id;
    
    @Column(name = "processed_at")
    private Instant processedAt;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    private MessageBatch batch;

}
