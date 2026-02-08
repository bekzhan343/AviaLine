package com.example.avialine.dto.response;

import com.example.avialine.dto.InfoPageDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoSubInfoResponse implements Serializable {

    @JsonProperty("information")
    private List<InfoPageDTO> information;


}
