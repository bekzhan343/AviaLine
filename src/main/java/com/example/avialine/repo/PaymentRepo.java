package com.example.avialine.repo;

import com.example.avialine.enums.PaymentStatus;
import com.example.avialine.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {

    boolean existsByOrderIdAndPaymentStatus(Integer orderId, PaymentStatus status);

    Set<Payment> getPaymentsByOrderId(Integer orderId);

}
