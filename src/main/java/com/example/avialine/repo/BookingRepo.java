package com.example.avialine.repo;

import com.example.avialine.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

    boolean existsByPnrNumber(String pnrNumber);

    @Query("""
    SELECT b FROM Booking b JOIN FETCH b.passengers ps
    WHERE b.pnrNumber = :pnrNumber
    AND LOWER(ps.surname) = LOWER(:surname)
""")
    Optional<Booking> findByPnrNumberAndSurname(
            @Param("pnrNumber") String pnrNumber,
            @Param("surname") String surname);

}
