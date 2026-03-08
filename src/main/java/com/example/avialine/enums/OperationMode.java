package com.example.avialine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OperationMode {

    VOLUNTARY,
    INVOLUNTARY;

    private String value;

}
