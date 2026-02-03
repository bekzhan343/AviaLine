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
        return UserDTO
                .builder()
                .firstName(user.getName())
                .email(user.getEmail())
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
