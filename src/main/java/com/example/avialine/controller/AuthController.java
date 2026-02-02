package com.example.avialine.controller;

import com.example.avialine.dto.request.ConfirmCodeRequest;
import com.example.avialine.dto.request.ConfirmEmailRequest;
import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.dto.response.ConfirmCodeResponse;
import com.example.avialine.dto.response.DefaultResponse;
import com.example.avialine.dto.response.DetailErrorResponse;
import com.example.avialine.dto.response.PersonInfoResponse;
import com.example.avialine.exception.InvalidCredentialsException;
import com.example.avialine.exception.UserNotFoundException;
import com.example.avialine.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@AllArgsConstructor
@RequestMapping("${end.point.auth-base}")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("${end.point.auth-login}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserProfileDTO> login(@RequestBody @Valid LoginRequest loginRequest){
        UserProfileDTO response = authService.login(loginRequest);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("${end.point.auth-register}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponse> register(@RequestBody @Valid RegisterRequest request){
        DefaultResponse response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("${end.point.auth-confirm-code}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> confirmCode(@RequestBody @Valid ConfirmCodeRequest request){
        try {
            ConfirmCodeResponse response = authService.confirmVerificationCode(request);

            return ResponseEntity.ok(response);
        }catch (InvalidCredentialsException  |UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DefaultResponse(
                    true,
                    e.getMessage()
                    )
            );
        }
    }

    @DeleteMapping("${end.point.auth-delete-user}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(){
        authService.deleteUser();

        return ResponseEntity.ok().build();
    }

    @GetMapping("${end.point.auth-personal-info}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PersonInfoResponse> getPersonalInfo(){
        PersonInfoResponse personalInfo = authService.getPersonalInfo();

        return ResponseEntity.ok(personalInfo);
    }

    @PostMapping("${end.point.auth-forgot-password}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponse> sendVerificationCode(@RequestBody ConfirmEmailRequest request){
        DefaultResponse response = authService.sendEmailVerificationCode(request);

        return ResponseEntity.ok(response);
    }

}