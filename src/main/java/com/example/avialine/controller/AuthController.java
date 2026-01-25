package com.example.avialine.controller;

import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.service.AuthService;
import com.example.avialine.wrapper.IamResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("${end.point.auth-base}")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("${end.point.auth-login}")
    public ResponseEntity<IamResponse<UserProfileDTO>> login(@RequestBody LoginRequest loginRequest){
        IamResponse<UserProfileDTO> response = authService.login(loginRequest);

        return ResponseEntity.ok(response);
    }



}