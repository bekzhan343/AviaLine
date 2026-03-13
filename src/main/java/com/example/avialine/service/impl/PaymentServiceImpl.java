package com.example.avialine.service.impl;

import com.example.avialine.dto.request.RefundQuoteRequest;
import com.example.avialine.dto.response.*;
import com.example.avialine.enums.*;
import com.example.avialine.exception.BadRequestException;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.model.entity.*;
import com.example.avialine.repo.*;
import com.example.avialine.security.util.SecurityUtil;
import com.example.avialine.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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
    private final UserService userService;
    private final OrderRepo orderRepo;
    private final RefundRepo refundRepo;
    private final BookingRepo bookingRepo;
    private final PassengerRepo passengerRepo;

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
    public RefundQuoteResponse refundQuote(RefundQuoteRequest request) {

        // 1. Находим бронь по regnum и surname
        Booking booking = bookingRepo.findByPnrNumberAndSurname(request.getRegnum(), request.getSurname())
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.BOOKING_NOT_FOUND_BY_PNR.getMessage()));

        Order order = booking.getOrder();

        // 2. Возврат только для оплаченных заказов
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException(ApiErrorMessage.REFUND_AVAIL_FOR_PAID_MESSAGE.getMessage());
        }

        // 3. Находим пассажиров
        List<Passenger> passengers = passengerRepo.findAllById(request.getPassengerIds());

        if (passengers.isEmpty()) {
            throw new DataNotFoundException("Пассажиры не найдены");
        }

        // 4. Проверяем что все пассажиры принадлежат этой брони
        boolean allMatch = passengers.stream()
                .allMatch(p -> p.getBooking().getId().equals(booking.getId()));

        if (!allMatch) {
            throw new BadRequestException("Один или несколько пассажиров не принадлежат этой брони");
        }

        // 5. Определяем статус возврата
        RefundStatus status = Boolean.TRUE.equals(request.getPretend())
                ? RefundStatus.PENDING
                : RefundStatus.COMPLETED;

        // 6. Считаем возврат для каждого пассажира
        List<Refund> refunds = new ArrayList<>();
        List<RefundQuoteResponse.PassengerRefund> passengerRefunds = new ArrayList<>();

        for (Passenger passenger : passengers) {

            BigDecimal passengerPrice = order.getTotalAmount()
                    .divide(BigDecimal.valueOf(order.getPassengerCount()), 2, RoundingMode.HALF_UP);

            BigDecimal penaltyAmount;
            BigDecimal refundAmount;

            if (Boolean.TRUE.equals(request.getFullRefund())) {
                penaltyAmount = BigDecimal.ZERO;
                refundAmount = passengerPrice;
            } else {
                penaltyAmount = passengerPrice.multiply(BigDecimal.valueOf(0.2));
                refundAmount = passengerPrice.subtract(penaltyAmount);
            }

            refunds.add(Refund.builder()
                    .order(order)
                    .passenger(passenger)
                    .refundAmount(refundAmount)
                    .penaltyAmount(penaltyAmount)
                    .operationMode(request.getOperationMode() != null
                            ? request.getOperationMode().name()
                            : OperationMode.VOLUNTARY.name())
                    .fullRefund(request.getFullRefund())
                    .pretend(request.getPretend())
                    .status(status)
                    .build());

            passengerRefunds.add(RefundQuoteResponse.PassengerRefund.builder()
                    .passengerId(passenger.getId())
                    .firstName(passenger.getFirstname())
                    .lastName(passenger.getLastname())
                    .refundAmount(refundAmount)
                    .penaltyAmount(penaltyAmount)
                    .build());
        }

        // 7. Если не pretend — реально сохраняем и меняем статусы
        if (Boolean.FALSE.equals(request.getPretend())) {
            refundRepo.saveAll(refunds); // saveAll вместо save в цикле
            order.setStatus(OrderStatus.REFUNDED);
            orderRepo.save(order);
        }

        // 8. Возвращаем ответ
        return RefundQuoteResponse.builder()
                .regnum(request.getRegnum())
                .status(status.toString())
                .operationMode(request.getOperationMode() != null
                        ? request.getOperationMode().name()
                        : OperationMode.VOLUNTARY.name())
                .pretend(request.getPretend())
                .totalRefundAmount(passengerRefunds.stream()
                        .map(RefundQuoteResponse.PassengerRefund::getRefundAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .passengers(passengerRefunds)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public GetPaymentsByOrderIdResponse getPaymentsByOrderId(Integer orderId) {

        Order order = orderService.getOrderById(orderId);

        Set<Payment> payments = paymentRepo.getPaymentsByOrderId(orderId);

        if (payments.isEmpty()){
            throw new DataNotFoundException(ApiErrorMessage.PAYMENTS_NOT_FOUND.getMessage());
        }
        Set<GetPaymentsByOrderIdResponse.PaymentResponse> response = payments
                .stream()
                .map(dtoMapper::toPaymentsResponse)
                .collect(Collectors.toSet());

        return GetPaymentsByOrderIdResponse.builder()
                .orderId(orderId)
                .orderStatus(order.getStatus())
                .payments(response)
                .build();
    }

    @Override
    public GetAllPaymentsByOrders paymentsHistoryByOrder() {

        Authentication auth = SecurityUtil.requireAuthentication();

        User user = userService.getUserByPhone(auth.getName());

        List<Booking> bookings = bookingService.getByUser(user);

        List<Order> orders = orderRepo.getOrderByBookingIn(bookings);

        List<GetAllPaymentsByOrders.OrderPaymentSummary> orderResponse = orders.stream()
                .map(


                        order -> {

                             List<GetAllPaymentsByOrders.PaymentDetailSummary> paymentDetailSummaries = order.getPayments().stream()
                                    .map(dtoMapper::toPaymentDetailSummary)
                                    .toList();

                             return GetAllPaymentsByOrders.OrderPaymentSummary
                                    .builder()
                                    .orderId(order.getId())
                                    .regnum(order.getRegnum())
                                    .orderStatus(order.getStatus())
                                    .totalAmount(order.getTotalAmount())
                                    .taxes(order.getTaxes())
                                    .passengersCount(order.getPassengerCount())
                                    .payments(paymentDetailSummaries)
                                    .build();
                        }
                )
                .toList();

        return GetAllPaymentsByOrders.builder()
                .userId(user.getId())
                .orders(orderResponse)
                .build();
    }

}
