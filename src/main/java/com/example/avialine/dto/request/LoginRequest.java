package com.example.avialine.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Component
public class LoginRequest implements Serializable {

    @NotBlank(message = "email cannot be empty!")
    private String email;

    @NotBlank(message = "password cannot be empty!")
    private String password;
}
