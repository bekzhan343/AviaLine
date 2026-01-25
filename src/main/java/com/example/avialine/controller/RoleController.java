package com.example.avialine.controller;

import com.example.avialine.dto.RoleDTO;
import com.example.avialine.service.RoleService;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${end.point.role-base}")
@AllArgsConstructor
@RestController
public class RoleController {

    private final RoleService roleService;

    @GetMapping("${end.point.role-id}")
    public ResponseEntity<IamResponse<RoleDTO>> getRoleById(@PathVariable("id") Integer id){
        IamResponse<RoleDTO> response = roleService.getRoleById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.role-create}")
    public ResponseEntity<IamResponse<RoleDTO>> createRole(@RequestBody @Valid RoleDTO dto){
        IamResponse<RoleDTO> response = roleService.createRole(dto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.point.role-update}")
    public ResponseEntity<IamResponse<RoleDTO>> updateRole(@PathVariable("id") Integer id, @RequestBody @Valid RoleDTO dto){
        IamResponse<RoleDTO> response = roleService.updateRoleById(id, dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.point.role-delete}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Integer id){
        roleService.deleteRoleById(id);

        return ResponseEntity.ok().build();
    }
}
