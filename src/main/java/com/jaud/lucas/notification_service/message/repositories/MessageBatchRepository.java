package com.jaud.lucas.notification_service.message.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaud.lucas.notification_service.message.entities.MessageBatch;

@Repository 
public interface MessageBatchRepository extends JpaRepository<MessageBatch, Integer> {   
}
