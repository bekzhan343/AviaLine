package com.example.avialine.service;

import com.example.avialine.dto.request.*;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.response.ConfirmCodeResponse;
import com.example.avialine.dto.response.DefaultResponse;
import com.example.avialine.dto.response.PersonInfoResponse;
import jakarta.validation.constraints.NotNull;


public interface AuthService {

    UserProfileDTO login(@NotNull LoginRequest loginRequest);

    DefaultResponse register(@NotNull RegisterRequest registerRequest);

    ConfirmCodeResponse confirmVerificationCode(@NotNull ConfirmCodeRequest confirmCodeRequest);

    void deleteUser();

    PersonInfoResponse getPersonalInfo();

    DefaultResponse forgotPassword(@NotNull ForgotPasswordSerializers request);

    DefaultResponse modifyPassword(@NotNull ModifyPasswordRequest request);

    DefaultResponse resendCodeToEmail(@NotNull ForgotPasswordSerializers request);

    DefaultResponse logout();
}
