package com.example.avialine.service.impl;

import com.example.avialine.dto.response.PaymentInitResponse;
import com.example.avialine.dto.response.PaymentStatusResponse;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.enums.OrderStatus;
import com.example.avialine.enums.PaymentStatus;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.exception.ValidationException;
import com.example.avialine.model.entity.Order;
import com.example.avialine.model.entity.Payment;
import com.example.avialine.repo.OrderRepo;
import com.example.avialine.repo.PaymentRepo;
import com.example.avialine.repo.ReceiptRepo;
import com.example.avialine.service.OrderService;
import com.example.avialine.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            throw new IllegalStateException(ApiErrorMessage.FORBIDDEN_FOR_CREATING_PAYMENT_MESSAGE.getMessage(order.getStatus()));
        }

        if (paymentRepo.existsByOrderIdAndPaymentStatus(orderId, PaymentStatus.PENDING)) {
            throw new IllegalStateException(ApiErrorMessage.PAYMENT_ALREADY_EXISTS_MESSAGE.getMessage());
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .currency(order.getCurrency())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        orderService.markAsPendingAndSave(order);

        Payment savedPayment = paymentRepo.save(payment);

        return PaymentInitResponse.builder()
                .paymentId(savedPayment.getId())
                .orderId(savedPayment.getOrder().getId())
                .amount(savedPayment.getAmount())
                .currency(savedPayment.getCurrency())
                .status(savedPayment.getPaymentStatus())
                .build();
    }

    @Override
    public PaymentStatusResponse paymentStatus(Integer orderId) {

        Payment payment = paymentRepo.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

        return PaymentStatusResponse.builder().paymentStatus(payment.getPaymentStatus().toString()).build();
    }

}
