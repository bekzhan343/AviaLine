package com.example.avialine.controller;

import com.example.avialine.dto.request.*;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.response.*;
import com.example.avialine.exception.*;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@AllArgsConstructor
@RequestMapping("${end.point.auth-base}")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("${end.point.auth-login}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserProfileDTO> login(@RequestBody @Valid LoginRequest loginRequest){
        UserProfileDTO response = authService.login(loginRequest);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("${end.point.auth-register}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponse> register(@RequestBody @Valid RegisterRequest request){
        DefaultResponse response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("${end.point.auth-confirm-code}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> confirmCode(@RequestBody @Valid ConfirmCodeRequest request){
        try {
            ConfirmCodeResponse response = authService.confirmVerificationCode(request);

            return ResponseEntity.ok(response);
        }catch (InvalidCredentialsException  | UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DefaultResponse(
                    false,
                    e.getMessage()
                    )
            );
        }
    }

    @DeleteMapping("${end.point.auth-delete-user}")
    public ResponseEntity<?> delete(){

        try {
            authService.deleteUser();
            return ResponseEntity.status(204).build();
        }catch (UserNotFoundException e){
            return ResponseEntity.status(401)
                    .body(
                            new DetailErrorResponse(
                                    ApiErrorMessage.NO_PROVIDED_ACCOUNT_MESSAGE.getMessage()
                            )
                    );
        }catch (BadCredentialsException | UserAlreadyDeletedException e){
            return ResponseEntity.status(401).body(
                    new DetailErrorResponse(
                            ApiErrorMessage.INVALID_CREDENTIALS_MESSAGE.getMessage()
                    )
            );
        }
    }

    @GetMapping("${end.point.auth-personal-info}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPersonalInfo(){

            PersonInfoResponse personalInfo = authService.getPersonalInfo();
            return ResponseEntity.ok(personalInfo);

    }

    @PostMapping("${end.point.auth-forgot-password}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponse> sendVerificationCode(@RequestBody ForgotPasswordSerializers request){
        DefaultResponse response = authService.forgotPassword(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.auth-modify-password}")
    public ResponseEntity<?> modifyPassword(@RequestBody @Valid ModifyPasswordRequest request){
        try {
            DefaultResponse response = authService.modifyPassword(request);
            return ResponseEntity.status(200).body(response);
        }catch (InvalidCredentialsException | BadCredentialsException e){
            return ResponseEntity.status(401).body(
                    new DetailErrorResponse(
                            ApiErrorMessage.NO_PROVIDED_ACCOUNT_MESSAGE.getMessage()
                    )
            );
        }catch (PasswordDoNotMatchException e){
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("error", List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(

                    new GlobalErrorResponse(
                            false,
                            ApiErrorMessage.ERROR_PROCESSING_REQUEST_MESSAGE.getMessage(),
                            errors
                    )
            );
        }
    }

    @PostMapping("${end.point.auth-re-send-email}")
    public ResponseEntity<DefaultResponse> resendEmail(@RequestBody ForgotPasswordSerializers request){
        try {

            DefaultResponse response = authService.resendCodeToEmail(request);

            return ResponseEntity.status(200).body(response);

        } catch (UserNotFoundException | InvalidCredentialsException e){
            return ResponseEntity.status(404).body(
                    new DefaultResponse(
                            false,
                            ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(request.getEmail())
                    )
            );
        }
    }

    @PostMapping("${end.point.auth-logout}")
    public ResponseEntity<DefaultResponse> logout(){
        try {
            DefaultResponse response = authService.logout();

            return ResponseEntity.status(200).body(response);
        }catch (InvalidCredentialsException | UnauthorizedException | BadCredentialsException e ){
            return ResponseEntity.status(400).body(
                    new DefaultResponse(
                            false,
                            ApiErrorMessage.NO_PROVIDED_ACCOUNT_MESSAGE.getMessage()
                    )
            );
        }
    }


    @PostMapping("${end.point.auth-modify-person-info}")
    public ResponseEntity<?> modifyPersonInfo(@RequestBody @Valid ModifyPersonInfoRequest request){
        try {
            DefaultResponse response = authService.modifyPersonInfo(request);

            return ResponseEntity.status(200).body(response);
        }catch (InvalidCredentialsException | BadCredentialsException e){
            return ResponseEntity.status(401).body(
                    new DetailErrorResponse(
                            ApiErrorMessage.INVALID_CREDENTIALS_MESSAGE.getMessage()
                    )
            );
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(409)
                    .body(
                            new DetailErrorResponse(
                                    ApiErrorMessage.USER_ALREADY_EXISTS_MESSAGE.getMessage(request.getFirstName())
                            )
                    );
        }catch (UnauthorizedException e){
            return ResponseEntity.status(401).body(
                    new DetailErrorResponse(
                            ApiErrorMessage.NO_PROVIDED_ACCOUNT_MESSAGE.getMessage()
                    )
            );
        }
    }

}