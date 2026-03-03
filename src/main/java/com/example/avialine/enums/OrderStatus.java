package com.example.avialine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OrderStatus {

    CREATED,
    PENDING,
    PAID,
    CANCELLED,
    ISSUED,
    REFUNDED,
    EXPIRED;

    private String value;
}
