package com.example.avialine.service.impl;

import com.example.avialine.dto.response.PaymentInitResponse;
import com.example.avialine.enums.PaymentStatus;
import com.example.avialine.model.entity.Order;
import com.example.avialine.model.entity.Payment;
import com.example.avialine.repo.PaymentRepo;
import com.example.avialine.repo.ReceiptRepo;
import com.example.avialine.service.OrderService;
import com.example.avialine.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderService orderService;
    private final PaymentRepo paymentRepo;
    private final ReceiptRepo receiptRepo;

    @Transactional
    @Override
    public PaymentInitResponse initPayment(Integer orderId) {

        Order order = orderService.getOrderById(orderId);

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .currency(order.getCurrency())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Payment savedPayment = paymentRepo.save(payment);

        return PaymentInitResponse.builder()
                .paymentId(savedPayment.getId())
                .orderId(savedPayment.getOrder().getId())
                .amount(savedPayment.getAmount())
                .currency(savedPayment.getCurrency())
                .status(savedPayment.getPaymentStatus())
                .build();
    }

}
