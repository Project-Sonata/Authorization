package com.odeyalo.sonata.authorization.exception;

/**
 * Can be thrown when confirmation has been failed
 */
public class RegistrationConfirmationFailedException extends RuntimeException {
    public RegistrationConfirmationFailedException(String message) {
        super(message);
    }

    public RegistrationConfirmationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
