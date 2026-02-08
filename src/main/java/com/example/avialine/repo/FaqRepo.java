package com.example.avialine.repo;

import com.example.avialine.model.entity.Faq;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepo extends JpaRepository<Faq, Integer> {

    Faq findBySlug(@NotNull String slug);

}
