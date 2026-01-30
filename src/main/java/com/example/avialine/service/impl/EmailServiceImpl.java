package com.example.avialine.service.impl;

import com.example.avialine.model.entity.VerificationCode;
import com.example.avialine.service.EmailService;
import com.example.avialine.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;


    private final VerificationCodeService verificationCodeService;
    private final JavaMailSender mailSender;


    @Override
    public String sendVerificationCode(String email) {

        VerificationCode code = verificationCodeService.createVerificationCode(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(email);
        message.setSubject("Verification Code");
        message.setText("Your verification code is " + code.getCode());

        try {
            mailSender.send(message);
        }catch (Exception e){
            log.error("Failed to send verification code to email: {}", email, e );
            throw new RuntimeException("Failed to send verification code.");
        }
        return "Verification code sent successfully!";
    }


    @Override
    public boolean verifyCode(String email, String code) {

         return verificationCodeService.verifyCode(email, code);
    }


}
