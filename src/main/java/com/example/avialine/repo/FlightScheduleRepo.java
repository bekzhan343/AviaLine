package com.example.avialine.repo;

import com.example.avialine.model.entity.Flight;
import com.example.avialine.model.entity.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightScheduleRepo extends JpaRepository<FlightSchedule, Integer> {

    @Query(
            """

                    SELECT fs FROM FlightSchedule fs
            JOIN FETCH fs.flight f
            WHERE f.departureCode = :departure
            AND f.arrivalCode = :arrival
            AND fs.date = :date
            AND fs.direct = :direct
            AND fs.available > 0
            """
    )
    List<FlightSchedule>  findAvailableFlights(
            @Param("departure") String departure,
            @Param("arrival") String arrival,
            @Param("date") LocalDate date,
            @Param("direct") Boolean direct
            );

    @Query("""
            
        SELECT fs FROM FlightSchedule fs
        JOIN FETCH fs.flight f
        WHERE f.departureCode = :departure
        AND f.arrivalCode = :arrival
""")
    List<FlightSchedule> findUpcomingFlights(String departure, String arrival);

    Optional<FlightSchedule> findByDateAndFlight(LocalDate date, Flight flight);

}
