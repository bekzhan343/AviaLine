package com.example.avialine.exception;

public class CodeAlreadyVerifiedException extends RuntimeException
{
    public CodeAlreadyVerifiedException(String message) {
        super(message);
    }
}
