package com.Vhytor.SmartRent.exceptions;

/**
 * Thrown when a viewing access code is invalid, expired, or has already been used.
 * Maps to HTTP 400 Bad Request.
 */
public class InvalidAccessCodeException extends SmartRentException {

    public InvalidAccessCodeException() {
        super("The access code is invalid, expired, or has already been used.");
    }

    public InvalidAccessCodeException(String message) {
        super(message);
    }
}
