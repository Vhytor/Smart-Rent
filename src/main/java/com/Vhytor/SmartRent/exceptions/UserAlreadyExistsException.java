package com.Vhytor.SmartRent.exceptions;

/**
 * Thrown when a user tries to register with an email that already exists.
 * Maps to HTTP 409 Conflict.
 */
public class UserAlreadyExistsException extends SmartRentException {

    public UserAlreadyExistsException(String email) {
        super("An account with email " + email + " already exists.");
    }
}
