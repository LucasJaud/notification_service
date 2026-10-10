package com.jaud.lucas.notification_service.message.entities;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@NoArgsConstructor 
@AllArgsConstructor 
@Getter 
@Setter 
@Table(name = "tb_message_batch")
public class MessageBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "batch")
    private Set<MessageBatchItem> messages;
    

    @Column(name = "created_at")
    private Instant createdAt;

    public void addMessage(MessageBatchItem message){
        this.messages.add(message);
    }
}
