package com.example.avialine.dto.response;

import com.example.avialine.dto.ServiceDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendServiceResponse implements Serializable {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @JsonProperty(value = "name", access = JsonProperty.Access.READ_ONLY)
    private String name;

    @JsonProperty(value = "image", access = JsonProperty.Access.READ_ONLY)
    private String image;

    @JsonProperty(value = "service", access = JsonProperty.Access.READ_ONLY)
    private ServiceDTO serviceDTO;
}
