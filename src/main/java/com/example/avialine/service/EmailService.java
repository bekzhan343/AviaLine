package com.example.avialine.service;

public interface EmailService {

    String sendVerificationCode(String email);

    boolean verifyCode(String email, String code);
}
