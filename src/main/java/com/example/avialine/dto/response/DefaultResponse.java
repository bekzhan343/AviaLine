package com.example.avialine.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class DefaultResponse {

    @JsonProperty("response")
    private boolean response;

    @JsonProperty("message")
    private String message;
}
