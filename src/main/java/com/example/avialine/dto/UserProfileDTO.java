package com.example.avialine.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class UserProfileDTO implements Serializable {

    private Integer id;

    private String name;

    private String email;

    private String password;

    private Instant createdAt;

    private Instant lastLogin;

    private String accessToken;

    private String refreshToken;
}
