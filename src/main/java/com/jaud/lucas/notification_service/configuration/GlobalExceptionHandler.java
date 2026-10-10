package com.jaud.lucas.notification_service.configuration;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jaud.lucas.notification_service.exceptions.CustomResponseException;
import com.jaud.lucas.notification_service.exceptions.InvalidPayloadException;
import com.jaud.lucas.notification_service.exceptions.MessageProcessingException;
import com.jaud.lucas.notification_service.exceptions.UsernameAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<CustomResponseException> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, HttpServletRequest request){
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(buildCustomReponse(request.getRequestURI(), ex.getClass().toString(), ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomResponseException> handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest request){
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(buildCustomReponse(request.getRequestURI(), ex.getName(), ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponseException> handleBadCredencialsException(BadCredentialsException ex, HttpServletRequest request){
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(buildCustomReponse(request.getRequestURI(), ex.getClass().toString(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidPayloadException.class)
    public ResponseEntity<CustomResponseException> handleInvalidPayloadException(InvalidPayloadException ex, HttpServletRequest request){
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(buildCustomReponse(request.getRequestURI(), ex.getClass().toString(), ex.getMessage()));
    }

    @ExceptionHandler(MessageProcessingException.class)
    public ResponseEntity<CustomResponseException> handleMessageProcessingException(MessageProcessingException ex, HttpServletRequest request){
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(buildCustomReponse(request.getRequestURI(), ex.getClass().toString(), ex.getMessage()));
    }

    private CustomResponseException buildCustomReponse(String path, String className, String exceptionMessage){
        return  new CustomResponseException(path, className, exceptionMessage, Instant.now());
    }
}
