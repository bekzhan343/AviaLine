package com.example.avialine.repo;


import com.example.avialine.model.entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingHistoryRepo extends JpaRepository<BookingHistory, Integer> {

    @Query("""
    SELECT bh FROM BookingHistory bh
    ORDER BY pnrVersion DESC
    LIMIT 1
""")
    Optional<BookingHistory> getByBookingId(Integer bookingId);

}
