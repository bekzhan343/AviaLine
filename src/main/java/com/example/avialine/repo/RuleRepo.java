package com.example.avialine.repo;

import com.example.avialine.model.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepo extends JpaRepository<Rule, Integer> {
}
