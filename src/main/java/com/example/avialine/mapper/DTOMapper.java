package com.example.avialine.mapper;

import com.example.avialine.dto.RoleDTO;
import com.example.avialine.dto.UserDTO;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public UserDTO toUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }

    public RoleDTO toRoleDTO(Role role){
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public UserProfileDTO toUserProfileDTO(User user,String accessToken){
        return UserProfileDTO
                .builder()
                .userId(user.getId())
                .token(accessToken)
                .phone(user.getPhone())
                .build();
    }
}
