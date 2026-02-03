package com.example.avialine.mapper;

import com.example.avialine.dto.RoleDTO;
import com.example.avialine.model.entity.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {



    public Role toRole(@NotNull RoleDTO dto){
        return Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
