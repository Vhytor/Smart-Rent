package com.Vhytor.SmartRent.exceptions;

public class EmailNotVerifiedException extends SmartRentException{
    private final String email;

    public EmailNotVerifiedException(String email) {
        super("EMAIL_NOT_VERIFIED:" + email);
        this.email = email;
    }

    public String getEmail() { return email; }
}
