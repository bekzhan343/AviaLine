package com.example.avialine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BookingStatus {

    CREATED("CREATED"),
    CANCELLED("CANCELLED"),
    PAID("PAID");

    private String status;


}
