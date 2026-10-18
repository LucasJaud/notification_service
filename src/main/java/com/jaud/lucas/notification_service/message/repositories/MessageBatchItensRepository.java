package com.jaud.lucas.notification_service.message.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jaud.lucas.notification_service.message.entities.MessageBatchItem;
import com.jaud.lucas.notification_service.message.entities.MessageStatus;

@Repository 
public interface MessageBatchItensRepository extends JpaRepository<MessageBatchItem, UUID>{
    
    @Modifying 
    @Query("UPDATE MessageBatchItem i SET i.status = :status WHERE i.id = :id") 
    void updateBatchItemStatus(UUID id, MessageStatus status);

}
