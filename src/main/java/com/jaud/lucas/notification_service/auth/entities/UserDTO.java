package com.jaud.lucas.notification_service.auth.entities;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(
    @NotBlank(message = "username cannot be empty") String username,
    @NotBlank(message = "password cannot be empty")  String password) {
    
}
