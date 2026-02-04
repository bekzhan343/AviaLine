package com.example.avialine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModifyPasswordRequest implements Serializable {

    @NotBlank(message = "password cannot be empty!")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "password cannot be empty!")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
