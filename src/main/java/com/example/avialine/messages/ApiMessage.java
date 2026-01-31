package com.example.avialine.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiMessage {

    VERIFICATION_CODE_SENT_MESSAGE("verification code sent to your email -> %s");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
