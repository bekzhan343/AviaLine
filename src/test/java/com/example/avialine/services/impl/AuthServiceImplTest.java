package com.example.avialine.services.impl;

import com.example.avialine.dto.request.ConfirmCodeRequest;
import com.example.avialine.dto.request.LoginRequest;
import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.dto.response.ConfirmCodeResponse;
import com.example.avialine.dto.response.DefaultResponse;
import com.example.avialine.dto.UserProfileDTO;
import com.example.avialine.exception.InvalidCredentialsException;
import com.example.avialine.exception.InvalidVerificationCodeException;
import com.example.avialine.exception.ValidationException;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.model.entity.RefreshToken;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.RefreshTokenRepo;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.security.provider.JwtTokenProvider;
import com.example.avialine.service.EmailService;
import com.example.avialine.service.UserService;
import com.example.avialine.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private AuthenticationManager authManager;
    @Mock private JwtTokenProvider tokenProvider;
    @Mock private UserRepo userRepo;
    @Mock private DTOMapper dtoMapper;
    @Mock private RefreshTokenRepo refreshTokenRepo;
    @Mock private EmailService emailService;
    @Mock private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "refreshTokenExpiration", 86400000L);
    }

    // ==================== REGISTER ====================

    @Test
    @DisplayName("register: успешная регистрация нового пользователя")
    void register_success() {
        RegisterRequest request = buildRegisterRequest();

        when(userRepo.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.empty());
        when(userRepo.existsByPhoneAndDeletedFalseAndEnabledTrue(request.getPhone())).thenReturn(false);

        User mockUser = buildUser(true);
        when(userService.createUser(request)).thenReturn(mockUser);
        when(userRepo.save(mockUser)).thenReturn(mockUser);

        DefaultResponse response = authService.register(request);

        assertTrue(response.isResponse());
        verify(emailService, times(1)).sendVerificationCode(request.getEmail());
        verify(userRepo, times(1)).save(mockUser);
    }

    @Test
    @DisplayName("register: email уже занят активным пользователем — ValidationException")
    void register_emailAlreadyExists_throwsValidationException() {
        RegisterRequest request = buildRegisterRequest();

        // enabled=true и emailVerified=true → isEnabled() вернёт true
        User activeUser = buildUser(true);

        when(userRepo.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.of(activeUser));

        assertThrows(ValidationException.class, () -> authService.register(request));
        verify(emailService, never()).sendVerificationCode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    @DisplayName("register: email не подтверждён — переотправляем код верификации")
    void register_emailNotVerified_resendCode() {
        RegisterRequest request = buildRegisterRequest();

        // enabled=false → isEnabled() вернёт false
        User inactiveUser = buildUser(false);

        when(userRepo.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.of(inactiveUser));

        DefaultResponse response = authService.register(request);

        assertTrue(response.isResponse());
        verify(emailService, times(1)).sendVerificationCode(request.getEmail());
        verify(userRepo, never()).save(any());
    }

    @Test
    @DisplayName("register: телефон уже занят — ValidationException")
    void register_phoneAlreadyExists_throwsValidationException() {
        RegisterRequest request = buildRegisterRequest();

        when(userRepo.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.empty());
        when(userRepo.existsByPhoneAndDeletedFalseAndEnabledTrue(request.getPhone())).thenReturn(true);

        assertThrows(ValidationException.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("register: пароли не совпадают — ValidationException")
    void register_passwordsDoNotMatch_throwsValidationException() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@gmail.com")
                .firstName("Test")
                .phone("+996700000000")
                .password("password123")
                .confirmPassword("different123")
                .build();

        when(userRepo.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.empty());
        when(userRepo.existsByPhoneAndDeletedFalseAndEnabledTrue(request.getPhone())).thenReturn(false);

        assertThrows(ValidationException.class, () -> authService.register(request));
    }

    // ==================== LOGIN ====================

    @Test
    @DisplayName("login: успешный вход")
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setPhone("+996700000000");
        request.setPassword("password123");

        Authentication mockAuth = mock(Authentication.class);
        User mockUser = buildUser(true);

        RefreshToken mockRefreshToken = RefreshToken.builder()
                .token("refresh-token")
                .user(mockUser)
                .build();

        when(authManager.authenticate(any())).thenReturn(mockAuth);
        when(tokenProvider.generateAccessToken(mockAuth)).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken(mockAuth)).thenReturn("refresh-token");
        when(userService.getActiveUserByPhone(request.getPhone())).thenReturn(mockUser);
        when(userRepo.save(mockUser)).thenReturn(mockUser);
        when(refreshTokenRepo.findByUserAndRevokedFalse(mockUser)).thenReturn(Optional.empty());
        when(refreshTokenRepo.save(any())).thenReturn(mockRefreshToken);
        when(dtoMapper.toUserProfileDTO(eq(mockUser), anyString())).thenReturn(new UserProfileDTO());

        UserProfileDTO result = authService.login(request);

        assertNotNull(result);
        verify(authManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("login: неверный пароль или телефон — InvalidCredentialsException")
    void login_invalidCredentials_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setPhone("+996700000000");
        request.setPassword("wrongpassword");

        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    // ==================== CONFIRM VERIFICATION CODE ====================

    @Test
    @DisplayName("confirmVerificationCode: успешное подтверждение")
    void confirmVerificationCode_success() {
        ConfirmCodeRequest request = new ConfirmCodeRequest();
        request.setEmail("test@gmail.com");
        request.setCode("123456");

        User mockUser = buildUser(false);

        RefreshToken mockRefreshToken = RefreshToken.builder()
                .token("refresh-token")
                .user(mockUser)
                .build();

        when(emailService.verifyCode(request.getEmail(), request.getCode())).thenReturn(true);
        when(userService.getActiveUserByEmail(request.getEmail())).thenReturn(mockUser);
        when(tokenProvider.generateAccessToken(any())).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken(any())).thenReturn("refresh-token");
        when(refreshTokenRepo.findByUserAndRevokedFalse(mockUser)).thenReturn(Optional.empty());
        when(refreshTokenRepo.save(any())).thenReturn(mockRefreshToken);

        ConfirmCodeResponse response = authService.confirmVerificationCode(request);

        assertTrue(response.isResponse());
        assertEquals("access-token", response.getToken());
        verify(emailService, times(1)).verifyCode(request.getEmail(), request.getCode());
        verify(userService, times(1)).getActiveUserByEmail(request.getEmail());
    }

    @Test
    @DisplayName("confirmVerificationCode: неверный код — InvalidVerificationCodeException")
    void confirmVerificationCode_invalidCode_throwsException() {
        ConfirmCodeRequest request = new ConfirmCodeRequest();
        request.setEmail("test@gmail.com");
        request.setCode("000000");

        when(emailService.verifyCode(request.getEmail(), request.getCode())).thenReturn(false);

        assertThrows(InvalidVerificationCodeException.class,
                () -> authService.confirmVerificationCode(request));

        // После исправления порядка в сервисе — getActiveUserByEmail не должен вызываться
        verify(userService, never()).getActiveUserByEmail(anyString());
    }

    // ==================== HELPERS ====================

    private RegisterRequest buildRegisterRequest() {
        return RegisterRequest.builder()
                .email("test@gmail.com")
                .firstName("Test")
                .phone("+996700000000")
                .password("password123")
                .confirmPassword("password123")
                .build();
    }

    /**
     * enabled=true  → isEnabled() вернёт true (enabled && emailVerified && !deleted)
     * enabled=false → isEnabled() вернёт false
     */
    private User buildUser(boolean enabled) {
        Role role = new Role();
        role.setName("ROLE_USER");

        return User.builder()
                .email("test@gmail.com")
                .phone("+996700000000")
                .name("Test")
                .enabled(enabled)
                .emailVerified(enabled)
                .deleted(false)
                .roles(Set.of(role))
                .build();
    }
}