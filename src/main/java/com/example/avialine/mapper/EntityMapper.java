package com.example.avialine.mapper;

import com.example.avialine.dto.RoleDTO;
import com.example.avialine.dto.UserDTO;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public User toUser(@NotNull UserDTO dto){
        return User.builder()
                .id(dto.getId())
                .name(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .deleted(false)
                .build();
    }

    public Role toRole(@NotNull RoleDTO dto){
        return Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
