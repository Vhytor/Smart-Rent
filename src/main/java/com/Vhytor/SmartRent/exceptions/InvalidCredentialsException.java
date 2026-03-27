package com.Vhytor.SmartRent.exceptions;

/**
 * Thrown when a user provides a wrong password during login.
 * Maps to HTTP 401 Unauthorized.
 */
public class InvalidCredentialsException extends SmartRentException{
    public InvalidCredentialsException() {
        super("The email or password you entered is incorrect.");
    }
}
