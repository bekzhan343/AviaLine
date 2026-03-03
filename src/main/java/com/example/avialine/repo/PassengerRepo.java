package com.example.avialine.repo;

import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByBookingId(Integer bookingId);

}
