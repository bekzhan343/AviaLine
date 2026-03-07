package com.example.avialine.dto.request;

import com.example.avialine.security.util.DateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddInfantRequest implements Serializable {

    @Length(min = 1, max = 6)
    @NotBlank(message = "regnum cannot be empty")
    private String regnum;

    @Length(min = 1, max = 60)
    private String firstname;

    @Length(min = 1, max = 60)
    private String surname;

    @Length(min = 1, max = 60)
    private String lastname;

    @JsonProperty("parent_pass_id")
    @NotNull(message = "parentPassId cannot be null")
    private Integer parentPassId;

    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate birthdate;

    @Pattern(regexp = "MALE|FEMALE")
    private String sex;

    @JsonProperty(defaultValue = "INF")
    private String category;

    @JsonProperty(value = "doc_code")
    @Pattern(regexp = "ПС|ПСП|СР|ZA|NP|NI", message = "Document code exactly must be on of these values{ПС/ПСП/СР/ZA/NP/NI}")
    @Length(min = 1)
    private String docCode;

    @Length(min = 1)
    private String doc;

    @Length(min = 1, max = 2)
    private String nationality;

    @JsonProperty("pspexpire")
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate pspExpire;

}
