package com.example.avialine.repo;

import com.example.avialine.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {


    Optional<Order> findByBookingId(Integer bookingId);

}
