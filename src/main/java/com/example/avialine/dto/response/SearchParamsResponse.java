package com.example.avialine.dto.response;

import com.example.avialine.dto.CityDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"id", "name", "code_name", "img", "cities"})
public class SearchParamsResponse implements Serializable {

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "id")
    private Integer id;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "name")
    private String name;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "code_name")
    private String codeName;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "img")
    private String image;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY, value = "cities")
    private List<CityDTO> cities;
}
