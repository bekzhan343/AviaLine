package com.example.avialine.repo;

import com.example.avialine.model.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Integer> {

    List<Passenger> findByBookingId(Integer bookingId);

}
