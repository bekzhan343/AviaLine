package com.example.avialine.service.impl;

import com.example.avialine.dto.UserDTO;
import com.example.avialine.dto.request.ConfirmCodeRequest;
import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.exception.*;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.RefreshToken;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.RefreshTokenRepo;
import com.example.avialine.repo.RoleRepo;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.security.provider.JwtTokenProvider;
import com.example.avialine.service.AuthService;
import com.example.avialine.service.EmailService;
import com.example.avialine.wrapper.IamResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;


    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final DTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepo refreshTokenRepo;
    private final EmailService emailService;

    private final String ROLE_USER = "ROLE_USER";

    @Override
    public IamResponse<UserProfileDTO> login(@NotNull LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String accessStr = tokenProvider.generateAccessToken(auth);
        String refreshStr = tokenProvider.generateRefreshToken(auth);

        User user = userRepo.findByEmailAndDeletedFalse(loginRequest.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException(ApiErrorMessage
                                .USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(loginRequest.getEmail())));

        String savedRefresh = saveRefreshTokenInDB(refreshStr, user);

        user.setLastLogin(Instant.now());
        userRepo.save(user);

        UserProfileDTO dto = dtoMapper.toUserProfileDTO(user, accessStr, savedRefresh);

        return IamResponse.createdSuccessfully(dto);
    }

    @Transactional
    @Override
    public IamResponse<UserDTO> register(@NotNull RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();


        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            throw new PasswordDoNotMatchException(ApiErrorMessage.PASSWORD_DO_NOT_MATCH_MESSAGE.getMessage());
        } else if (userRepo.existsByEmailAndDeletedFalse(email)) {
            throw new UserAlreadyExistsException(ApiErrorMessage.USER_ALREADY_EXISTS_MESSAGE.getMessage(email));
        }
        User user = setUser(registerRequest);

        User savedUser = userRepo.save(user);

        UserDTO response = dtoMapper.toUserDTO(savedUser);

        emailService.sendVerificationCode(user.getEmail());

        return IamResponse.createdSuccessfully(response);
    }

    @Override
    public IamResponse<String> confirmCode(@NotNull ConfirmCodeRequest request) {

        boolean isVerified = emailService.verifyCode(request.getEmail(), request.getCode());
        if (isVerified){
            return IamResponse.createdSuccessfully("Code verified");
        }
        return IamResponse.createdSuccessfully("Code is not verified!");
    }

    private User setUser(RegisterRequest request){
        return User
                .builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(getDefaultRoles())
                .createdAt(Instant.now())
                .lastLogin(null)
                .enabled(false)
                .emailVerified(false)
                .build();
    }


    private Set<Role> getDefaultRoles() {
        Role role = roleRepo.findByName(ROLE_USER)
                .orElseThrow(
                        () -> new RoleNotFoundException(
                                ApiErrorMessage
                                        .ROLE_NOT_FOUND_BY_NAME_MESSAGE
                                        .getMessage(ROLE_USER)));

        return new HashSet<>(Set.of(role));
    }


    private String saveRefreshTokenInDB(@NotNull String refreshStr, User user){
        refreshTokenRepo
                .findByUserAndRevokedFalse(user)
                        .ifPresent(token -> {
                            token.setRevoked(true);
                            refreshTokenRepo.save(token);
                        });


        RefreshToken token = RefreshToken.builder()
                .token(refreshStr)
                .user(user)
                .createdAt(Instant.now())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        RefreshToken savedToken = refreshTokenRepo.save(token);
        return savedToken.getToken();
    }
}
