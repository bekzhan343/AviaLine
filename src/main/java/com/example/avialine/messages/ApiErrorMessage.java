package com.example.avialine.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {
    USER_NOT_FOUND_MESSAGE("user not found by id -> %s !"),
    USER_NOT_FOUND_BY_EMAIL_MESSAGE("user not found by email -> %s !"),
    USER_NOT_FOUND_BY_PHONE_MESSAGE("user not found by phone -> %s !"),
    UNAUTHORIZED_MESSAGE("user is not authenticated!"),
    USER_ALREADY_EXISTS_MESSAGE("user already exists by email -> %s !"),
    USER_NOT_ENABLED_MESSAGE("user is not enabled!"),

    ROLE_NOT_FOUND_BY_ID_MESSAGE("role not found by id -> %s !"),
    ROLE_NOT_FOUND_BY_NAME_MESSAGE("role not found by name -> %s !"),
    ROLE_ALREADY_EXISTS_MESSAGE("role already exists by name -> %s !"),

    TOKEN_EXPIRED_MESSAGE("token expired!"),
    TOKEN_NOT_FOUND_BY_USER_MESSAGE("token not found by id -> %s !"),

    INVALID_TOKEN_MESSAGE("invalid token"),
    INVALID_CODE_MESSAGE("invalid code -> %s !"),
    CODE_ALREADY_VERIFIED_MESSAGE("code already verified!"),

    EXPIRED_CODE_MESSAGE("expired code -> %s !"),
    ERROR_PROCESSING_REQUEST_MESSAGE("error processing request"),

    PHONE_NUMBER_IS_UNAVAILABLE_MESSAGE("phone number is unavailable!"),
    PASSWORD_DO_NOT_MATCH_MESSAGE("password do not match!"),
    EMAIL_NOT_VERIFIED_MESSAGE("email not verified!"),

    INVALID_PASSWORD_OR_PHONE_MESSAGE("invalid phone number or password!");


    private final String message;

    public String getMessage(Object... args) {
        return String.format(message,args);
    }
}
