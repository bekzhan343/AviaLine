package com.example.avialine.service;


import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.model.entity.User;
import jakarta.validation.constraints.NotNull;

public interface UserService {

    User createUser(RegisterRequest request);

    void deleteUserByEmail(@NotNull String email);

    User getActiveUserByEmail(@NotNull String email);

    User getActiveUserByPhone(@NotNull String phone);
}
