package com.example.avialine.repo;

import com.example.avialine.model.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    boolean existsByName(@NotNull String name);

    boolean existsByEmail(@NotNull String email);

    Optional<User> findByEmail(@NotNull String email);

    Optional<User> findByEmailAndDeletedFalse(@NotNull String email);

    boolean existsByEmailAndDeletedFalse(@NotNull String email);

    boolean existsByPhoneAndDeletedFalse(@NotNull String phone);
}
