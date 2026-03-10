package com.example.avialine.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING,    // создан
    PAID,       // оплачен
    FAILED,     // не прошёл
    CANCELLED,  // отменён
    REFUNDED    // возврат
}