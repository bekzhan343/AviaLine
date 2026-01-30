package com.example.avialine.repo;

import com.example.avialine.model.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepo extends JpaRepository<VerificationCode, Integer> {

    void deleteByUserEmailAndVerifiedFalse(String userEmail);

    Optional<VerificationCode> findByUserEmailAndCodeAndVerifiedFalse(String userEmail, String verificationCode);

}
