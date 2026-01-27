package com.example.avialine.service.impl;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.exception.PasswordDoNotMatchException;
import com.example.avialine.exception.UserAlreadyExistsException;
import com.example.avialine.exception.UserNotFoundException;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.security.provider.JwtTokenProvider;
import com.example.avialine.service.AuthService;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepo userRepo;
    private final DTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public IamResponse<UserProfileDTO> login(@NotNull LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String accessToken = tokenProvider.generateAccessToken(auth);
        String refreshToken = tokenProvider.generateRefreshToken(auth);


        User user = userRepo.findByEmailAndDeletedFalse(loginRequest.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException(ApiErrorMessage
                                .USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(loginRequest.getEmail())));

        UserProfileDTO dto = dtoMapper.toUserProfileDTO(user, accessToken, refreshToken);

        return IamResponse.createdSuccessfully(dto);
    }

    @Override
    public IamResponse<UserDTO> register(@NotNull RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            throw new PasswordDoNotMatchException(ApiErrorMessage.PASSWORD_DO_NOT_MATCH_MESSAGE.getMessage());
        } else if (userRepo.existsByEmail(email)) {
            throw new UserAlreadyExistsException(ApiErrorMessage.USER_ALREADY_EXISTS_MESSAGE.getMessage(email));
        }

        User user = User.builder()
                .name(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .email(email)
                .phone(registerRequest.getPhone())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .lastLogin(null)
                .enabled(true)
                .deleted(false)
                .build();

        User savedUser = userRepo.save(user);

        UserDTO dto = dtoMapper.toUserDTO(savedUser);


        return IamResponse.createdSuccessfully(dto);
    }


}
