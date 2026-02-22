package com.example.avialine.service.impl;


import com.example.avialine.exception.CodeAlreadyVerifiedException;
import com.example.avialine.exception.InvalidCredentialsException;
import com.example.avialine.exception.InvalidVerificationCodeException;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.model.entity.User;
import com.example.avialine.model.entity.VerificationCode;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.repo.VerificationCodeRepo;
import com.example.avialine.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class VerificationCodeServiceImpl  implements VerificationCodeService {

    @Value("${code.expiration}")
    private long expiration;

    private final UserRepo userRepo;
    private final VerificationCodeRepo verificationCodeRepo;
    private final SecureRandom random = new SecureRandom();



    @Transactional
    @Override
    public VerificationCode createVerificationCode(String userEmail) {

        verificationCodeRepo
                .deleteByUserEmailAndVerifiedFalse(userEmail); /* эта часть кода отвечает за удаление ненужных
                                                                   или не подтвержденных кодов подтверждения
                                                                   чтобы не оказать особую нагрузку базу данным */

        String email = getUser(userEmail).getEmail();

        Instant now = Instant.now();

        VerificationCode code = new VerificationCode();
        code.setUserEmail(email);
        code.setCode(generateRandomCode());
        code.setCreatedAt(now);
        code.setExpiresAt(now.plusMillis(expiration));
        code.setVerified(false);
        code.setVerifiedAt(null);


        return verificationCodeRepo.save(code);
    }

    @Transactional
    @Override
    public boolean verifyCode(String userEmail, String code) {

        VerificationCode vCode = getVerificationCode(userEmail, code);


        if (vCode.isVerified()){
            throw new CodeAlreadyVerifiedException(ApiErrorMessage.CODE_ALREADY_VERIFIED_MESSAGE.getMessage());
        }

        if (vCode.getExpiresAt().isBefore(Instant.now())){
            throw new InvalidVerificationCodeException(
                    ApiErrorMessage
                            .EXPIRED_CODE_MESSAGE
                            .getMessage(vCode.getCode())
            );
        }


        vCode.setVerified(true);
        vCode.setVerifiedAt(Instant.now());
        verificationCodeRepo.save(vCode);

        User user = getUser(userEmail);
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setLastLogin(Instant.now());

        userRepo.save(user);

        return true;
    }


    private String generateRandomCode(){
        int code = 100000 + random.nextInt(900000);

        return String.valueOf(code);
    }

    private User getUser(String email){
        return userRepo.findByEmailAndDeletedFalse(email)
                .orElseThrow(
                        () -> new InvalidCredentialsException(
                                ApiErrorMessage
                                        .USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(email))

                );
    }

    private VerificationCode getVerificationCode(String userEmail, String code){
        return verificationCodeRepo
                .findByUserEmailAndCodeAndVerifiedFalse(userEmail, code)
                .orElseThrow(
                        () -> new InvalidVerificationCodeException(
                                ApiErrorMessage
                                        .INVALID_CODE_MESSAGE
                                        .getMessage(code)));

    }
}
