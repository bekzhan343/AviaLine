package com.example.avialine.repo;

import com.example.avialine.model.entity.SubInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubInfoRepo extends JpaRepository<SubInfo, Integer> {
}
