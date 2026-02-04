package com.example.avialine.repo;

import com.example.avialine.model.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    boolean existsByName(@NotNull String name);

    boolean existsByEmail(@NotNull String email);

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

    boolean existsByEmailAndDeletedFalse(@NotNull String email);

    boolean existsByPhoneAndDeletedFalse(@NotNull String phone);


}
