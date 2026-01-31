package com.example.avialine.service;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.dto.request.ConfirmCodeRequest;
import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.constraints.NotNull;


public interface AuthService {

    IamResponse<UserProfileDTO> login(@NotNull LoginRequest loginRequest);

    IamResponse<UserDTO> register(@NotNull RegisterRequest registerRequest);

    IamResponse<String> confirmCode(@NotNull ConfirmCodeRequest confirmCodeRequest);

    void deleteUser();
}
