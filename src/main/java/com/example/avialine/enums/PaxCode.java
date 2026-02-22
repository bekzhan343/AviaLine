package com.example.avialine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaxCode {
    ADT("Взрослый", 12, 999),
    CHD("Ребенок", 2, 11),
    INF("Младенец без места", 0, 1),
    INS("Младенец с местом", 0, 1);

    private final String name;
    private final int min;
    private final int max;

}
