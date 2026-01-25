package com.example.avialine.controller;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.service.UserService;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${end.point.user-base}")
@AllArgsConstructor
@RestController
public class UserController {

    private UserService userService;

    @GetMapping("${end.point.user-id}")
    public ResponseEntity<IamResponse<UserDTO>> getUserById(@PathVariable("id") @Valid Integer id){
        IamResponse<UserDTO> response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.user-create}")
    public ResponseEntity<IamResponse<UserDTO>> postUser(@Valid @RequestBody UserDTO userDTO){
        IamResponse<UserDTO> response = userService.createUser(userDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.point.user-update}")
    public ResponseEntity<IamResponse<UserDTO>> putUser(@PathVariable("email") String email,
                                                        @Valid @RequestBody UserDTO userDTO){
        IamResponse<UserDTO> response = userService.updateUserByEmail(email, userDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.point.user-delete}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id){
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
