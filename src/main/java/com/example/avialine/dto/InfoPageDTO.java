package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoPageDTO implements Serializable {

    @JsonProperty("language")
    private String language;

    @JsonProperty("title")
    private String title;

    @JsonProperty("img")
    private String image;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("background_color")
    private String backgroundColor;

    @JsonProperty("description")
    private String description;

    @JsonProperty("subinfo")
    private List<SubInfoDTO> subInfos;
}
