package com.example.avialine.repo;

import com.example.avialine.model.entity.PrivacyPolice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacyPoliceRepo extends JpaRepository<PrivacyPolice, Integer> {
}
