package com.jaud.lucas.notification_service.message;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api/v1/message")
public class MessageController {

    @PostMapping("/add-message")
    public ResponseEntity<Void> addMessage(){
        return ResponseEntity.accepted().build();
    }
    
}
