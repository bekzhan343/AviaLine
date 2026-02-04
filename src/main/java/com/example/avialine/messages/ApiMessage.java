package com.example.avialine.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiMessage {

    SUCCESSFULLY_MODIFIED_PASSWORD_MESSAGE("you successfully modified your password"),
    VERIFICATION_CODE_SENT_MESSAGE("verification code sent to your email!"),
    CODE_CONFIRMED_MESSAGE("code confirmed, account activated successfully!");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
