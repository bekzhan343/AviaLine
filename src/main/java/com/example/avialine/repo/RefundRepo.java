package com.example.avialine.repo;

import com.example.avialine.model.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// RefundRepo
@Repository
public interface RefundRepo extends JpaRepository<Refund, Integer> {
    List<Refund> findAllByOrderId(Integer orderId);
}