package com.example.avialine.dto.request;

import lombok.*;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class RegisterRequest {

    private String username;

    private String email;

    private String phone;

    private String password;

    private String confirmPassword;
}
