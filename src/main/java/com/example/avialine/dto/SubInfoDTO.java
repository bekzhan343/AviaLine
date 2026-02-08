package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubInfoDTO implements Serializable {

    @JsonProperty("title")
    private String title;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("description")
    private String description;

}
