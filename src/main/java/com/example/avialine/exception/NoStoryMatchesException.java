package com.example.avialine.exception;

public class NoStoryMatchesException extends RuntimeException {
    public NoStoryMatchesException(String message) {
        super(message);
    }
}
