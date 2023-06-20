package com.odeyalo.sonata.authorization.exception;

import com.odeyalo.sonata.common.authentication.exception.InvalidCredentialsException;
import com.odeyalo.sonata.common.authentication.exception.RegistrationFailedException;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorWebExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ResponseEntity.badRequest().body(e.getErrorDetails());
    }

    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<ErrorDetails> handleRegistrationFailedException(RegistrationFailedException e) {
        return ResponseEntity.badRequest().body(e.getErrorDetails());
    }
}