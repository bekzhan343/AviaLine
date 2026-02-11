package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PopularDirectsResponse implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("language")
    private String language;

    @JsonProperty("code_to")
    private String codeTo;

    @JsonProperty("code_from")
    private String codeFrom;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("image")
    private String image;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
