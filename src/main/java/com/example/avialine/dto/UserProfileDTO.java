package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class UserProfileDTO implements Serializable {

    @JsonProperty("token")
    private String token;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("phone")
    private String phone;
}
