 package com.jaud.lucas.notification_service.message.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaud.lucas.notification_service.message.entities.ProcessedMessage;

@Repository 
public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, UUID>{
    

    
}