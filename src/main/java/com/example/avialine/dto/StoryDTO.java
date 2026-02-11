package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoryDTO implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("language")
    private String language;

    @JsonProperty("img_pc")
    private String imgPc;

    @JsonProperty("img_mb")
    private String imgMB;

    @JsonProperty("title")
    private String title;

    @JsonProperty("detailed")
    private String detailed;

    @JsonProperty("description")
    private String description;
}
