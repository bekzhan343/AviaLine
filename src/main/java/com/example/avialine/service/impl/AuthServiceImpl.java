package com.example.avialine.service.impl;

import com.example.avialine.dto.request.*;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.dto.response.ConfirmCodeResponse;
import com.example.avialine.dto.response.DefaultResponse;
import com.example.avialine.dto.response.PersonInfoResponse;
import com.example.avialine.exception.*;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.messages.ApiMessage;
import com.example.avialine.model.entity.RefreshToken;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.RefreshTokenRepo;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.security.provider.JwtTokenProvider;
import com.example.avialine.security.util.SecurityUtil;
import com.example.avialine.service.AuthService;
import com.example.avialine.service.EmailService;
import com.example.avialine.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;


    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepo userRepo;
    private final DTOMapper dtoMapper;
    private final RefreshTokenRepo refreshTokenRepo;
    private final EmailService emailService;
    private final UserService userService;

    private final String ROLE_USER = "ROLE_USER";

    @Transactional
    @Override
    public UserProfileDTO login(@NotNull LoginRequest loginRequest) {

        try {

            Instant now = Instant.now();

            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getPhone(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            String accessStr = tokenProvider.generateAccessToken(auth);
            String refreshStr = tokenProvider.generateRefreshToken(auth);

            User user = userService.getActiveUserByPhone(loginRequest.getPhone());

            saveRefreshTokenInDB(refreshStr, user);

            user.setLastLogin(now);
            userRepo.save(user);


            return  dtoMapper.toUserProfileDTO(user, accessStr);

        }catch (BadCredentialsException  | UserNotFoundException e){
            throw new InvalidCredentialsException(ApiErrorMessage.INVALID_PASSWORD_OR_PHONE_MESSAGE.getMessage());
        }
    }

    @Transactional
    @Override
    public DefaultResponse register(@NotNull RegisterRequest registerRequest) {

        Map<String, List<String>> errors = new HashMap<>();
        String email = registerRequest.getEmail();


        if (userRepo.existsByEmailAndDeletedFalse(email)) {
            errors.put("email", List.of(ApiErrorMessage.USER_ALREADY_EXISTS_MESSAGE.getMessage(email)));
        }
        if (userRepo.existsByPhoneAndDeletedFalse(registerRequest.getPhone())){
            errors.put("phone", List.of(ApiErrorMessage.PHONE_NUMBER_IS_UNAVAILABLE_MESSAGE.getMessage()));
        }
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            errors.put("password", List.of(ApiErrorMessage.PASSWORD_DO_NOT_MATCH_MESSAGE.getMessage()));
        }

        if (!errors.isEmpty()){
            throw new  ValidationException("error: ", errors);
        }

        User user = userService.createUser(registerRequest);

        userRepo.save(user);

        emailService.sendVerificationCode(user.getEmail());

        return new DefaultResponse(true, ApiMessage.VERIFICATION_CODE_SENT_MESSAGE.getMessage());
    }

    @Override
    public ConfirmCodeResponse confirmVerificationCode(@NotNull ConfirmCodeRequest request) {

            boolean isVerified = emailService.verifyCode(request.getEmail(), request.getCode());
            User user = userService.getActiveUserByEmail(request.getEmail());
            if (!isVerified) {
                throw new InvalidVerificationCodeException(ApiErrorMessage.CODE_ALREADY_VERIFIED_MESSAGE.getMessage());
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getPhone(),
                    null,
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            String accessToken = tokenProvider.generateAccessToken(auth);
            String refreshToken = tokenProvider.generateRefreshToken(auth);

            saveRefreshTokenInDB(refreshToken, user);

            return new ConfirmCodeResponse(
                    true,
                    ApiMessage.CODE_CONFIRMED_MESSAGE.getMessage(),
                    accessToken
            );

    }

    @Override
    public void deleteUser() {
        Authentication auth = SecurityUtil.requireAuthentication();

        String phone = auth.getName();

        log.info("phone: " + phone);

        User user = userService.getUserByPhone(phone);

        boolean isActive = userService.validateUserForDeletion(user);

        if (!isActive){
            throw new UserAlreadyDeletedException(
                    ApiErrorMessage
                            .USER_ALREADY_DELETED_MESSAGE.getMessage()
            );
        }

        userService.deleteUser(user);

        List<RefreshToken> tokens = refreshTokenRepo.findAllByUserAndRevokedFalse(user);
        tokens.forEach(token -> token.setRevoked(true));

        refreshTokenRepo.saveAll(tokens);

    }

    @Override
    public PersonInfoResponse getPersonalInfo() {

        Authentication auth = SecurityUtil.requireAuthentication();

        String phone = auth.getName();

        User user = userService.getActiveUserByPhone(phone);

        return PersonInfoResponse
                .builder()
                .response(true)
                .data(dtoMapper.toUserDTO(user))
                .build();
    }

    @Override
    public DefaultResponse forgotPassword(@NotNull ForgotPasswordSerializers request){

        User user = userService.getActiveUserByEmail(request.getEmail());

        emailService.sendVerificationCode(user.getEmail());


        return new DefaultResponse(
                true,
                ApiMessage.VERIFICATION_CODE_SENT_MESSAGE.getMessage(user.getEmail())
        );


    }

    @Transactional
    @Override
    public DefaultResponse modifyPassword(@NotNull ModifyPasswordRequest request) {

        Authentication auth = SecurityUtil.requireAuthentication();

        String phone = auth.getName();

        User user = userService.getActiveUserByPhone(phone);

        userService.changeUserPassword(user, request);

        return new DefaultResponse(
                true,
                ApiMessage.SUCCESSFULLY_MODIFIED_PASSWORD_MESSAGE.getMessage()
        );
    }

    @Override
    public DefaultResponse resendCodeToEmail(ForgotPasswordSerializers request) {
        User user = userService.getUserByEmailAndDeletedFalse(request.getEmail());

        emailService.sendVerificationCode(user.getEmail());

        return new DefaultResponse(
                true,
                ApiMessage.VERIFICATION_CODE_SENT_MESSAGE.getMessage()
        );
    }

    @Override
    public DefaultResponse logout() {
        Authentication auth = SecurityUtil.requireAuthentication();

        String phone = auth.getName();

        User user = userService.getActiveUserByPhone(phone);

        List<RefreshToken> tokens = refreshTokenRepo.findAllByUserAndRevokedFalse(user);

        tokens.forEach(token -> token.setRevoked(true));

        refreshTokenRepo.saveAll(tokens);

        SecurityContextHolder.getContext().setAuthentication(null);

        return new DefaultResponse(
                true,
                ApiMessage.LOGOUT_DONE_MESSAGE.getMessage()
        );
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
