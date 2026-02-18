package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportDTO implements Serializable {

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "id")
    private Integer id;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "name")
    private String name;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "code_name")
    private String codeName;

}
