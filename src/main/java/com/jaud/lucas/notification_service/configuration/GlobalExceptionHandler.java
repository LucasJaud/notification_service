package com.jaud.lucas.notification_service.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jaud.lucas.notification_service.exceptions.CustomResponseException;
import com.jaud.lucas.notification_service.exceptions.UsernameAlreadyExistsException;

@ControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<CustomResponseException> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseException(ex.getClass().toString(), ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomResponseException> handleUsernameNotFoundException(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseException(ex.getName(), ex.getMessage()));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponseException> handleBadCredencialsException(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseException(ex.getClass().toString(), ex.getMessage()));
    }
}
