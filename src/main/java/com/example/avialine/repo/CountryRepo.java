package com.example.avialine.repo;

import com.example.avialine.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CountryRepo extends JpaRepository<Country, Integer> {

    @Query("""
    SELECT DISTINCT c 
    FROM Country c 
    JOIN FETCH c.cities city 
    JOIN FETCH city.airports
""")
    List<Country> findCountryWithAirports();

}
