package com.example.avialine.service;


import com.example.avialine.dto.UserDTO;
import jakarta.validation.constraints.NotNull;

public interface UserService {

    UserDTO getUserById(@NotNull Integer id);

    UserDTO createUser(@NotNull UserDTO userDTO);

    UserDTO updateUserByEmail(@NotNull String email, @NotNull UserDTO dto);

    void deleteUserById(@NotNull Integer id);


}
