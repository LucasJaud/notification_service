package com.jaud.lucas.notification_service.auth;

import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaud.lucas.notification_service.auth.entities.AuthRequest;
import com.jaud.lucas.notification_service.auth.entities.AuthResponse;
import com.jaud.lucas.notification_service.auth.entities.User;
import com.jaud.lucas.notification_service.auth.entities.UserDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/hello") 
    public String hello() {
        return "Hello from auth controller";
    }
    
    @GetMapping("/default-user") 
    public User getDefaultUser() {
        return authService.getUser("admin");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid UserDTO authRequest) {        
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/register") 
    public ResponseEntity<User> register(@RequestBody @Valid UserDTO user){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(user));
    }
    

}
