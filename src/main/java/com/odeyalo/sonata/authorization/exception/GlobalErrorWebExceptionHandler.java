package com.odeyalo.sonata.authorization.exception;

import com.odeyalo.sonata.authorization.dto.FailedRegistrationConfirmationDto;
import com.odeyalo.sonata.common.authentication.exception.EmailConfirmationRequiredException;
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

    @ExceptionHandler(RegistrationConfirmationFailedException.class)
    public ResponseEntity<FailedRegistrationConfirmationDto> handleRegistrationConfirmationFailedException(RegistrationConfirmationFailedException ex) {
        FailedRegistrationConfirmationDto dto = FailedRegistrationConfirmationDto.failed();
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(EmailConfirmationRequiredException.class)
    public ResponseEntity<ErrorDetails> handleEmailConfirmationRequiredException(EmailConfirmationRequiredException ex) {
        return ResponseEntity.badRequest().body(ex.getErrorDetails());
    }
}