package com.example.avialine.exception;

public class PasswordDoNotMatchException extends RuntimeException {
    public PasswordDoNotMatchException(String message) {
        super(message);
    }
}
