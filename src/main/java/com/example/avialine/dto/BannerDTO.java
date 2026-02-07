package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO implements Serializable {

    @JsonProperty("language")
    private String language;

    @JsonProperty("image")
    private String image;

    @JsonProperty("image_mobile")
    private String imageMobile;

    @JsonProperty("title")
    private String title;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("description")
    private String description;
}
