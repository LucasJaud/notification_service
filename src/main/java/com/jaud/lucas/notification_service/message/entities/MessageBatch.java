package com.jaud.lucas.notification_service.message.entities;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Data 
@Table(name = "tb_message_batch")
public class MessageBatch {

    @Id 
    private Integer id;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private Set<MessageBatchItem> messages;
    

    @Column(name = "created_at")
    private Instant createdAt;

    public void addMessage(MessageBatchItem message){
        this.messages.add(message);
    }
}
