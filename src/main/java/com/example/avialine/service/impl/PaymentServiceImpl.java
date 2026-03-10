package com.example.avialine.service.impl;

import com.example.avialine.dto.response.PayResponse;
import com.example.avialine.dto.response.PaymentInitResponse;
import com.example.avialine.dto.response.PaymentStatusResponse;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.enums.OrderStatus;
import com.example.avialine.enums.PaymentStatus;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.model.entity.Order;
import com.example.avialine.model.entity.Payment;
import com.example.avialine.repo.PaymentRepo;
import com.example.avialine.repo.ReceiptRepo;
import com.example.avialine.service.MockGatewayService;
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
    private final MockGatewayService mockGatewayService;

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

    @Override
    public PayResponse pay(Integer paymentId) {

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

        if (!payment.getPaymentStatus().equals(PaymentStatus.PENDING)) {
            throw new IllegalStateException(ApiErrorMessage.FORBIDDEN_FOR_PAY_MESSAGE.getMessage(payment.getPaymentStatus()));
        }

        boolean success = mockGatewayService.process(payment);

        if (success){
            payment.setPaymentStatus(PaymentStatus.PAID);
            payment.setPaidAmount(payment.getAmount());
            orderService.markAsPaidAndSave(payment.getOrder());
        }else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }

        Payment savedPayment = paymentRepo.save(payment);

        return PayResponse.builder()
                .paymentId(savedPayment.getId())
                .status(savedPayment.getPaymentStatus())
                .amount(savedPayment.getAmount())
                .paidAmount(savedPayment.getPaidAmount())
                .build();
    }

}
