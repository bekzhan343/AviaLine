package com.example.avialine.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {
    USER_NOT_FOUND_MESSAGE("user not found by id -> %s !"),
    USER_NOT_FOUND_BY_EMAIL_MESSAGE("user not found by email -> %s !"),

    ROLE_NOT_FOUND_BY_ID_MESSAGE("role not found by id -> %s !"),
    ROLE_NOT_FOUND_BY_NAME_MESSAGE("role not found by name -> %s !"),

    USER_ALREADY_EXISTS_MESSAGE("user already exists by email -> %s !"),
    ROLE_ALREADY_EXISTS_MESSAGE("role already exists by name -> %s !"),

    TOKEN_EXPIRED_MESSAGE("token expired!"),
    INVALID_TOKEN_MESSAGE("invalid token"),
    TOKEN_NOT_FOUND_BY_USER_MESSAGE("token not found by id -> %s !"),

    INVALID_CODE_MESSAGE("invalid code -> %s !"),
    EXPIRED_CODE_MESSAGE("expired code -> %s !"),

    PASSWORD_DO_NOT_MATCH_MESSAGE("password do not match!");
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message,args);
    }
}
