package com.example.avialine.repo;

import com.example.avialine.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CountryRepo extends JpaRepository<Country, Integer> {


}
