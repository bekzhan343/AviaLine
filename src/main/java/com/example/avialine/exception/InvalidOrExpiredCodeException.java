package com.example.avialine.exception;

public class InvalidOrExpiredCodeException extends RuntimeException {
    public InvalidOrExpiredCodeException(String message) {
        super(message);
    }
}
