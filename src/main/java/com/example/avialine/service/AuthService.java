package com.example.avialine.service;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.dto.request.ConfirmCodeRequest;
import com.example.avialine.dto.request.ConfirmEmailRequest;
import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.dto.response.ConfirmEmailResponse;
import com.example.avialine.dto.response.DefaultResponse;
import com.example.avialine.dto.response.PersonInfoResponse;
import jakarta.validation.constraints.NotNull;


public interface AuthService {

    UserProfileDTO login(@NotNull LoginRequest loginRequest);

    DefaultResponse register(@NotNull RegisterRequest registerRequest);

    String confirmVerificationCode(@NotNull ConfirmCodeRequest confirmCodeRequest);

    void deleteUser();

    PersonInfoResponse getPersonalInfo();

    ConfirmEmailResponse sendEmailVerificationCode(@NotNull ConfirmEmailRequest request);

}
