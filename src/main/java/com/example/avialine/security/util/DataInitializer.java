package com.example.avialine.security.util;

import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.RoleRepo;
import com.example.avialine.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.phone}")
    private String adminPhone;

    @Value("${app.admin.password}")
    private String adminPassword;


    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepo userRepo, RoleRepo roleRepo,  PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = passwordEncoder;
    }

    private static final String roleAdmin = "ROLE_ADMIN";
    private static final EmailValidator emailValidator = EmailValidator.getInstance();

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        String email = validateEmail(adminEmail);
        String phoneNumber = validatePhoneNumber(adminPhone);
        String  password = validatePassword(adminPassword);
        initAdmin(phoneNumber, password, email);
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(ApiErrorMessage.EMAIL_REQUIRED_MESSAGE.getMessage());
        }

        if (!emailValidator.isValid(email)){
            throw new IllegalArgumentException(ApiErrorMessage.INVALID_EMAIL_FORMAT_MESSAGE.getMessage());
        }

        return email;
    }

    private String validatePassword(String adminPassword){

        if (adminPassword == null || adminPassword.trim().isEmpty()){
            throw new IllegalArgumentException(ApiErrorMessage.PASSWORD_CANNOT_BE_EMPTY_MESSAGE.getMessage());
        }

        if (adminPassword.length() < 8 || adminPassword.contains(" ")){
            throw new IllegalArgumentException(ApiErrorMessage.INVALID_PASSWORD_FORMAT_MESSAGE.getMessage());
        }

        return adminPassword;
    }

    private String validatePhoneNumber(String adminPhone){
        if (adminPhone == null || adminPhone.trim().isEmpty()){
            throw new IllegalArgumentException(ApiErrorMessage.PHONE_NUMBER_IS_UNAVAILABLE_MESSAGE.getMessage());
        }

        adminPhone = adminPhone.trim().replaceAll("[^0-9+]", "");


        if (adminPhone.matches("0\\d{9}")){
            return "+996" + adminPhone.substring(1);
        }

        if (adminPhone.matches("996\\d{9}")){
            return "+" + adminPhone;
        }

        if (adminPhone.matches("\\+996\\d{9}")){
            return adminPhone;
        }

        throw new IllegalArgumentException(ApiErrorMessage.INVALID_PHONE_FORMAT_MESSAGE.getMessage());
    }

    private void initAdmin(String phone, String password, String email){
        Role adminRole = roleRepo.findByName(roleAdmin)
                .orElseGet(
                        () -> roleRepo
                                .save(
                                        Role
                                                .builder()
                                                .name(roleAdmin)
                                                .build()
                                )
                );

        if (!userRepo.existsByRolesContainingAndDeletedFalse(adminRole)){
            User user = User
                    .builder()
                    .name(adminName)
                    .email(email)
                    .phone(phone)
                    .password(encoder.encode(password))
                    .roles(Set.of(adminRole))
                    .deleted(false)
                    .enabled(true)
                    .emailVerified(true)
                    .createdAt(Instant.now())
                    .lastLogin(null)
                    .build();

            userRepo.save(user);
        }
    }



}
