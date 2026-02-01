package com.example.avialine.service;

import com.example.avialine.dto.RoleDTO;
import jakarta.validation.constraints.NotNull;

public interface RoleService {

    RoleDTO getRoleById(@NotNull Integer id);

    RoleDTO createRole(@NotNull RoleDTO roleDTO);

    RoleDTO updateRoleById(@NotNull Integer id,@NotNull RoleDTO roleDTO);

    void deleteRoleById(@NotNull Integer id);
}
