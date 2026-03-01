package com.example.avialine.repo;

import com.example.avialine.model.entity.Flight;
import jakarta.validation.constraints.Past;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepo extends JpaRepository<Flight, Integer> {


    @Query("""
    SELECT f FROM Flight f
    WHERE f.carrier = :company
    AND f.flightNumber = :flightNumber
    AND f.departureCode = :departure
    AND f.arrivalCode = :arrival
""")
    Optional<Flight> findFlightByParameters(@Param("company") String company,
                                            @Param("flightNumber") String flightNumber,
                                            @Param("departure") String departure,
                                            @Param("arrival") String arrival);



}

