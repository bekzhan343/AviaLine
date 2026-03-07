package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PnrVersionResponse implements Serializable {

    @JsonProperty("pnr_version")
    private Integer pnrVersion;

    @JsonProperty("change_type")
    private String changeType;

    private String comment;


}
