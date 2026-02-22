package com.example.avialine.service.impl;

import com.example.avialine.dto.RoleDTO;
import com.example.avialine.exception.RoleAlreadyExistsException;
import com.example.avialine.exception.RoleNotFoundException;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.mapper.EntityMapper;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.model.entity.Role;
import com.example.avialine.repo.RoleRepo;
import com.example.avialine.service.RoleService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;
    private final DTOMapper dtoMapper;
    private final EntityMapper entityMapper;

    @Override
    public RoleDTO getRoleById(@NotNull Integer id) {
        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(ApiErrorMessage.ROLE_NOT_FOUND_BY_ID_MESSAGE.getMessage(id)));

        return  dtoMapper.toRoleDTO(role);
    }

    @Override
    public RoleDTO createRole(@NotNull RoleDTO roleDTO) {

        if (roleRepo.existsByName(roleDTO.getName())){
            throw new RoleAlreadyExistsException(ApiErrorMessage.ROLE_ALREADY_EXISTS_MESSAGE.getMessage(roleDTO.getName()));
        }

        Role role = entityMapper.toRole(roleDTO);

        Role savedRole = roleRepo.save(role);

        return dtoMapper.toRoleDTO(savedRole);
    }

    @Override
    public RoleDTO updateRoleById(@NotNull Integer id,@NotNull RoleDTO roleDTO) {

        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(ApiErrorMessage.ROLE_NOT_FOUND_BY_ID_MESSAGE.getMessage(roleDTO.getId())));

        role.setName(roleDTO.getName());

        Role updatedRole = roleRepo.save(role);

        return dtoMapper.toRoleDTO(updatedRole);
    }

    @Override
    public void deleteRoleById(@NotNull Integer id) {
        roleRepo.deleteById(id);
    }
}
