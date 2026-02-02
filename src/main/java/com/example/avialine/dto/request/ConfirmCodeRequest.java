package com.example.avialine.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class ConfirmCodeRequest {

    @NotBlank(message = "email cannot be empty!")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "code cannot be empty!")
    @Pattern(regexp = "\\d{6}", message = "code must be 6 digits")
    private String code;
}
