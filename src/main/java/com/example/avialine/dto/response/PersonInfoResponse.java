package com.example.avialine.dto.response;

import com.example.avialine.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class PersonInfoResponse implements Serializable {

    @JsonProperty("response")
    private boolean response;

    @JsonProperty("data")
    private UserDTO data;

}
