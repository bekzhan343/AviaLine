package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqAnswerResponse implements Serializable {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answer")
    private String answer;
}
