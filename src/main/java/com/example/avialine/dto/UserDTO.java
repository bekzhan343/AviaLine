package com.example.avialine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Component
public class UserDTO implements Serializable {

    private Integer id;

    @NotBlank(message = "username cannot be empty!")
    private String username;

    @NotBlank(message = "password cannot be empty!")
    private String password;


    @NotBlank(message = "email cannot be empty!")
    private String email;

    @NotBlank(message = "phone cannot be empty!")
    private String phone;

    private Instant createdAt;

    private Instant lastLogin;
}
