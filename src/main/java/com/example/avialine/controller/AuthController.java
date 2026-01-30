package com.example.avialine.controller;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.dto.request.ConfirmCodeRequest;
import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.service.AuthService;
import com.example.avialine.wrapper.IamResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

    @PostMapping("${end.point.auth-register}")
    public ResponseEntity<IamResponse<UserDTO>> register(@RequestBody RegisterRequest request){
        IamResponse<UserDTO> response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.auth-confirm-code}")
    public ResponseEntity<IamResponse<String>> confirmCode(@RequestBody ConfirmCodeRequest request){
        IamResponse<String> response = authService.confirmCode(request);

        return ResponseEntity.ok(response);
    }


}