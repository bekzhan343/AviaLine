package com.example.avialine.repo;

import com.example.avialine.model.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepo extends JpaRepository<Doc, Integer> {

    @Query("SELECT d FROM Doc d join fetch d.countries")
    List<Doc> findAllWithCountries();
}
