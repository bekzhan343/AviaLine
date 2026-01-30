package com.example.avialine.service;

import com.example.avialine.model.entity.VerificationCode;

public interface VerificationCodeService {

    VerificationCode createVerificationCode(String email);

    boolean verifyCode(String userEmail, String code);
}
