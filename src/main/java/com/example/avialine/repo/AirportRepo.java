package com.example.avialine.repo;

import com.example.avialine.model.entity.Airport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepo extends JpaRepository<Airport, Integer> {
}
