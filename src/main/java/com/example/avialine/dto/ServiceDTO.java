package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceDTO implements Serializable {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @JsonProperty(value = "name", access = JsonProperty.Access.READ_ONLY)
    private String name;

    @JsonProperty(value = "description",access = JsonProperty.Access.READ_ONLY)
    private String description;
}

