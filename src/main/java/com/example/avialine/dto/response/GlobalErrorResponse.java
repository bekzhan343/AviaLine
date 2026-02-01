package com.example.avialine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalErrorResponse {

    private boolean response = false;

    private String message;

    private Map<String, List<String>> errors;
}
