package com.example.avialine.service;


import com.example.avialine.dto.UserDTO;
import com.example.avialine.model.entity.User;
import jakarta.validation.constraints.NotNull;

public interface UserService {

    UserDTO getUserById(@NotNull Integer id);

    UserDTO createUser(@NotNull UserDTO userDTO);

    UserDTO updateUserByEmail(@NotNull String email, @NotNull UserDTO dto);

    void deleteUserByEmail(@NotNull String email);

    User getActiveUserByEmail(@NotNull String email);

    User getActiveUserByPhone(@NotNull String phone);
}
