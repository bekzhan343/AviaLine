package com.example.avialine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPersonInfoRequest {

    @JsonProperty("first_name")
    @Size(min = 3, max = 50)
    @NotBlank(message = "first_name cannot be empty!")
    private String firstName;


    @JsonProperty("email")
    @NotBlank(message = "email cannot be empty!")
    @Email(message = "invalid email format!")
    @Size(min = 3, max = 50)
    private String email;

}
