package com.example.avialine.repo;

import com.example.avialine.model.entity.BookingSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSegmentRepo extends JpaRepository<BookingSegment, Integer> {


}
