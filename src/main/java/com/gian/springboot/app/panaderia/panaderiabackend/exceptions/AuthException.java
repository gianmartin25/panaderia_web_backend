package com.gian.springboot.app.panaderia.panaderiabackend.exceptions;

public class AuthException extends RuntimeException {
    private final int statusCode;

    public AuthException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}