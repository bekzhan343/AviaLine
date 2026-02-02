package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("response")
    private boolean response = false;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error")
    private Map<String, List<String>> error;
}
