package com.example.avialine.repo;

import com.example.avialine.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

    boolean existsByPnrNumber(String pnrNumber);

}
