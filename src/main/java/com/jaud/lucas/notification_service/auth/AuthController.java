package com.jaud.lucas.notification_service.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api/v1/auth")
public class AuthController {

    public String hello() {
        return "Hello from auth controller";
    }
    

}
