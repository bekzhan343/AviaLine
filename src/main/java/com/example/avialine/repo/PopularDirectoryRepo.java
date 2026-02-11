package com.example.avialine.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularDirectoryRepo extends JpaRepository<com.example.avialine.model.entity.PopularDirectory, Long> {
}
