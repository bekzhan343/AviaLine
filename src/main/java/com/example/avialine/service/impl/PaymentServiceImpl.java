package com.example.avialine.service.impl;

import com.example.avialine.dto.response.*;
import com.example.avialine.enums.*;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.model.entity.Order;
import com.example.avialine.model.entity.Payment;
import com.example.avialine.repo.PaymentRepo;
import com.example.avialine.repo.ReceiptRepo;
import com.example.avialine.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderService orderService;
    private final PaymentRepo paymentRepo;
    private final MockGatewayService mockGatewayService;
    private final BookingService bookingService;
    private final DTOMapper dtoMapper;

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

    @Transactional(readOnly = true)
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

    @Override
    public PayResponse retry(Integer paymentId) {

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

        if (!payment.getPaymentStatus().equals(PaymentStatus.FAILED)) {
            throw new IllegalStateException(ApiErrorMessage.RETRY_NOT_AVAIL_STATUS_MESSAGE.getMessage(payment.getPaymentStatus()));
        }

        boolean success = mockGatewayService.process(payment);

        if (success){
            payment.setPaymentStatus(PaymentStatus.PAID);
            payment.setPaidAmount(payment.getAmount());
        }else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }

        paymentRepo.save(payment);

        return PayResponse.builder()
                .paymentId(paymentId)
                .status(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .paidAmount(payment.getPaidAmount())
                .build();
    }

    @Override
    public PayCancelResponse cancel(Integer paymentId) {

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

        if (payment.getPaymentStatus() != PaymentStatus.PENDING && payment.getPaymentStatus() !=(PaymentStatus.FAILED))  {
            throw new IllegalStateException(ApiErrorMessage.CANCEL_NOT_AVAIL_STATUS_MESSAGE.getMessage(payment.getPaymentStatus()));
        }

        payment.setPaymentStatus(PaymentStatus.CANCELLED);

        paymentRepo.save(payment);
        orderService.markAsCancelledAndSave(payment.getOrder());
        bookingService.markAsCancelledAndSave(payment.getOrder().getBooking());

        return PayCancelResponse.builder()
                .paymentId(payment.getId())
                .message(ApiMessage.PAYMENT_CANCELLED.getMessage())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }

    @Transactional
    @Override
    public PayRefundResponse refund(Integer paymentId) {

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

        if (payment.getPaymentStatus() != PaymentStatus.PAID){
            throw new IllegalStateException(ApiErrorMessage.REFUND_NOT_AVAIL_STATUS_MESSAGE.getMessage(payment.getPaymentStatus()));
        }

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setPaidAmount(BigDecimal.valueOf(0));

        orderService.markAsRefundAndSave(payment.getOrder());
        bookingService.markAsRefundAndSave(payment.getOrder().getBooking());

        return PayRefundResponse.builder()
                .paymentId(payment.getId())
                .message(ApiMessage.PAYMENT_REFUND_SUCCESS.getMessage(payment.getPaidAmount()) + payment.getCurrency())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public GetPaymentsResponse getPayments(Integer orderId) {

        Set<Payment> payments = paymentRepo.getPaymentsByOrderId(orderId);

        if (payments.isEmpty()){
            throw new DataNotFoundException(ApiErrorMessage.PAYMENTS_NOT_FOUND.getMessage());
        }
        Set<GetPaymentsResponse.PaymentsResponse> response = payments
                .stream()
                .map(dtoMapper::toPaymentsResponse)
                .collect(Collectors.toSet());

        return GetPaymentsResponse.builder()
                .orderId(orderId)
                .payments(response)
                .build();
    }

}
