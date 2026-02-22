package com.example.avialine.repo;

import com.example.avialine.model.entity.Role;
import com.example.avialine.model.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    Optional<User> findUserByPhone(@NotNull String phone);

    Optional<User> findByEmailAndDeletedFalse(@NotNull String email);

    @Query("""
        SELECT u FROM User u
        WHERE u.email = :email
            AND u.deleted = false
            AND u.enabled = true
            AND u.emailVerified = true
    """)
    Optional<User> findActiveUserByEmail(String email);


    @Query("""
        SELECT u FROM User u
        WHERE u.phone = :phone
            AND u.deleted = false
            AND u.enabled = true
            AND u.emailVerified = true
    """)
    Optional<User> findActiveUserByPhone(String phone);

    boolean existsByPhoneAndDeletedFalseAndEnabledTrue(@NotNull String phone);

    boolean existsByEmailAndEnabledTrueAndDeletedFalse(@NotNull String email);

    boolean existsByRolesContainingAndDeletedFalse(@NotNull Role role);



}
