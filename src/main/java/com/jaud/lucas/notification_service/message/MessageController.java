package com.jaud.lucas.notification_service.message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaud.lucas.notification_service.message.dtos.MessageBatchDTO;
import com.jaud.lucas.notification_service.message.dtos.RequestBatchMessage;

@RestController 
@RequestMapping("/api/v1/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/add-message")
    public ResponseEntity<MessageBatchDTO> addMessage(@RequestBody RequestBatchMessage request){

        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createBatchMessage(request));
    }
    
}
