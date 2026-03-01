package com.example.avialine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@Data
public class DepArrRequest implements Serializable {


    @JsonProperty("dep")
    private String departure;

    @JsonProperty("arr")
    private String arrival;
}
