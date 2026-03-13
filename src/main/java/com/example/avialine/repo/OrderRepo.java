package com.example.avialine.repo;

import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {


    Optional<Order> findByBookingId(Integer bookingId);

    Optional<Order> findOrderByRegnum(String regnum);

    @EntityGraph(attributePaths = "payments")
    List<Order> getOrderByBookingIn(List<Booking> bookings);
}
