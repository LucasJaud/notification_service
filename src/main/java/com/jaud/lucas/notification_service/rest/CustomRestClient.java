package com.jaud.lucas.notification_service.rest;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.jaud.lucas.notification_service.message.Message;

public class CustomRestClient {
    
    
    
    public ResponseEntity<Void> sendRequest(Message message){
        RestClient client = RestClient.create();

        return client
            .post()
            .uri(URI.create(message.getUrlString()))
            .body(message.getPayload())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toBodilessEntity();
    }
}

// IllegalArgumentException
// RestClientException
