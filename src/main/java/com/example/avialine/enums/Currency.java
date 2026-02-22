package com.example.avialine.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {

    RUB("RUB"),
    KGZ("KGZ"),
    USD("USD"),
    KZT("KZT");
    @Schema(example = "USD")

    private final String symbol;
}
