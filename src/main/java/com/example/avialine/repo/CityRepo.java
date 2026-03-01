package com.example.avialine.repo;

import com.example.avialine.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CityRepo extends JpaRepository<City, Integer> {


    City findCityByCode(String code);
}
