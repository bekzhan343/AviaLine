package com.example.avialine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @Schema(
            example = "user@gmail.com",
            description = "Email",
            title = "Эл-почта (логин)",
            maxLength = 254,
            nullable = true
    )
    @Email(message = "Invalid email format!")
    @JsonProperty("email")
    @Size(max = 254, message = "Email must be at most 254 chars!")
    @NotBlank(message = "email cannot be empty!")
    private String email;


    @Schema(
            description = "Имя пользователя",
            title = "Имя",
            maxLength = 100,
            minLength = 1
    )
    @NotBlank(message = "first_name cannot be empty!")
    @JsonProperty("first_name")
    @Size(max = 100, min = 1, message = "first_name must be at least 1 char and at most 100")
    private String firstName;


    @Schema(
            description = "Номер телефона",
            title = "Phone",
            minLength = 1
    )
    @NotBlank(message = "phone cannot be empty!")
    @JsonProperty("phone")
    @Size(min = 1, message = "phone must be at least 1 char!")
    @Pattern(regexp = "^\\+996\\d{9}$",
            message = "Invalid phone number! Expected format: +996XXXXXXXXX"
    )
    private String phone;

    @Schema(
            description = "Пароль",
            title = "Пароль",
            maxLength = 128,
            minLength = 1
    )
    @NotBlank(message = "password cannot be empty!")
    @JsonProperty("password")
    @Size(min = 1, max = 128, message = "invalid password!")
    private String password;



    @Schema(
            description = "Пароль подтверждения",
            title = "Confirm password",
            minLength = 8
    )
    @NotBlank(message = "password cannot be empty!")
    @JsonProperty("confirm_password")
    @Size(min = 8, message = "invalid password!")
    private String confirmPassword;
}
