package com.example.avialine.service;

import com.example.avialine.dto.RoleDTO;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.constraints.NotNull;

public interface RoleService {

    IamResponse<RoleDTO> getRoleById(@NotNull Integer id);

    IamResponse<RoleDTO> createRole(@NotNull RoleDTO roleDTO);

    IamResponse<RoleDTO> updateRoleById(@NotNull Integer id,@NotNull RoleDTO roleDTO);

    void deleteRoleById(@NotNull Integer id);
}
