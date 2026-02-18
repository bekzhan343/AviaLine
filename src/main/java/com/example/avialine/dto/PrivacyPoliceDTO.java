package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrivacyPoliceDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "id")
    private Integer id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "title")
    private String title;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "file")
    private String file;
}
