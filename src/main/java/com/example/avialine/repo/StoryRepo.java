package com.example.avialine.repo;

import com.example.avialine.model.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepo extends JpaRepository<Story, Integer> {
}
