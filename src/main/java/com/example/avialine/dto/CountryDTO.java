package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CountryDTO implements Serializable {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;
}
