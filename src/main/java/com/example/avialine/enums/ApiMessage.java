package com.example.avialine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiMessage {

    SUCCESSFULLY_MODIFIED_PASSWORD_MESSAGE("you successfully modified your password"),
    VERIFICATION_CODE_SENT_MESSAGE("verification code sent to your email!"),
    CODE_CONFIRMED_MESSAGE("code confirmed, account activated successfully!"),
    LOGOUT_DONE_MESSAGE("you have been logged out!"),
    PERSON_INFO_MODIFIED_MESSAGE("you successfully modified your personal info successfully !"),
    BOOKING_CREATED_COMMENT("booking has been created, the regnum %s "),
    ADDED_INFANT_MESSAGE("you successfully added infant to the booking with regnum %s ");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
