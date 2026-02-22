package com.example.avialine.repo;

import com.example.avialine.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    boolean existsByName(String name);

    @Query("SELECT DISTINCT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(String name);

}
