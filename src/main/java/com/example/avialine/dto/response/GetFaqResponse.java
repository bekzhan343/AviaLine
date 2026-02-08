package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFaqResponse implements Serializable {

    @JsonProperty("language")
    private String language;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("question")
    private String question;
}
