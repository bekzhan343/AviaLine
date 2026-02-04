package com.example.avialine.service.impl;

import com.example.avialine.dto.request.RegisterRequest;
import com.example.avialine.exception.*;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.mapper.EntityMapper;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.RoleRepo;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final DTOMapper dtoMapper;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;

    private final String ROLE_USER = "ROLE_USER";


    @Override
    public User createUser(RegisterRequest request) {
        return User
                .builder()
                .name(request.getFirstName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(getDefaultRole())
                .createdAt(Instant.now())
                .lastLogin(null)
                .enabled(false)
                .emailVerified(false)
                .build();
    }

    @Transactional
    @Override
    public void deleteUser(@NotNull User user) {

        user.setDeleted(true);

        userRepo.save(user);
    }

    @Transactional
    @Override
    public User getActiveUserByEmail(String email) {
        return userRepo.findActiveUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(email))
                );

    }

    @Transactional(readOnly = true)
    @Override
    public User getActiveUserByPhone(String phone) {
        return userRepo.findActiveUserByPhone(phone)
                .orElseThrow(() -> new InvalidCredentialsException(
                        ApiErrorMessage.USER_NOT_FOUND_BY_PHONE_MESSAGE.getMessage(phone)
                )
                );
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepo
                .findUserByPhone(phone)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                ApiErrorMessage
                                        .USER_NOT_FOUND_BY_PHONE_MESSAGE.getMessage(phone))

                );
    }

    @Override
    public boolean validateUserForDeletion(User user) {
        if (user.isEnabled() && user.isEmailVerified() && !user.isDeleted()) {
            return true;
        }
        return false;
    }

    private Set<Role> getDefaultRole() {
        Role role = roleRepo.findByName(ROLE_USER)
                .orElseThrow(
                        () -> new RoleNotFoundException(
                                ApiErrorMessage
                                        .ROLE_NOT_FOUND_BY_NAME_MESSAGE
                                        .getMessage(ROLE_USER)));

        return new HashSet<>(Set.of(role));
    }
}
