package com.example.avialine.repo;

import com.example.avialine.model.entity.RefreshToken;
import com.example.avialine.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByUserAndRevokedFalse(User user);
}
