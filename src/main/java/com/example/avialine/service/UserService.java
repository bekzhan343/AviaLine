package com.example.avialine.service;


import com.example.avialine.dto.UserDTO;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.constraints.NotNull;

public interface UserService {

    IamResponse<UserDTO> getUserById(@NotNull Integer id);

    IamResponse<UserDTO> createUser(@NotNull UserDTO userDTO);

    IamResponse<UserDTO> updateUserByEmail(@NotNull String email, @NotNull UserDTO dto);

    void deleteUserById(@NotNull Integer id);
}
