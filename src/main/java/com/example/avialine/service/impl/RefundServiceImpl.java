package com.example.avialine.service.impl;

import com.example.avialine.dto.request.RefundQuoteRequest;
import com.example.avialine.dto.response.RefundQuoteResponse;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.enums.OrderStatus;
import com.example.avialine.enums.RefundStatus;
import com.example.avialine.exception.BadRequestException;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.Order;
import com.example.avialine.model.entity.Passenger;
import com.example.avialine.model.entity.Refund;
import com.example.avialine.repo.BookingRepo;
import com.example.avialine.repo.OrderRepo;
import com.example.avialine.repo.PassengerRepo;
import com.example.avialine.repo.RefundRepo;
import com.example.avialine.service.RefundService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefundServiceImpl implements RefundService {

    private final OrderRepo orderRepo;
    private final PassengerRepo passengerRepo;
    private final RefundRepo refundRepo;
    private final BookingRepo bookingRepo;

    @Transactional
    @Override
    public RefundQuoteResponse refundQuote(RefundQuoteRequest request) {

        // Находим бронь по regnum и surname
        Booking booking = bookingRepo.findByPnrNumberAndSurname(request.getRegnum(), request.getSurname())
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.BOOKING_NOT_FOUND_BY_PNR.getMessage()));

        Order order = booking.getOrder();

        // Возврат только для оплаченных
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException(ApiErrorMessage.REFUND_AVAIL_FOR_PAID_MESSAGE.getMessage());
        }

        List<Passenger> passengers = passengerRepo.findAllById(request.getPassengerIds());

        if (passengers.isEmpty()) {
            throw new DataNotFoundException("Пассажиры не найдены");
        }

        // Проверяем что все пассажиры принадлежат этой брони
        boolean allMatch = passengers.stream()
                .allMatch(p -> p.getBooking().getId().equals(booking.getId()));

        if (!allMatch) {
            throw new BadRequestException("Один или несколько пассажиров не принадлежат этой брони");
        }

        RefundStatus status = Boolean.TRUE.equals(request.getPretend())
                ? RefundStatus.PENDING
                : RefundStatus.COMPLETED;

        List<RefundQuoteResponse.PassengerRefund> passengerRefunds = new ArrayList<>();

        for (Passenger passenger : passengers) {

            // Цена на одного пассажира
            BigDecimal passengerPrice = order.getTotalAmount()
                    .divide(BigDecimal.valueOf(order.getPassengerCount()), 2, RoundingMode.HALF_UP);

            // Штраф и сумма возврата
            BigDecimal penaltyAmount;
            BigDecimal refundAmount;

            if (Boolean.TRUE.equals(request.getFullRefund())) {
                penaltyAmount = BigDecimal.ZERO;
                refundAmount = passengerPrice;
            } else {
                penaltyAmount = passengerPrice.multiply(BigDecimal.valueOf(0.2));
                refundAmount = passengerPrice.subtract(penaltyAmount);
            }

            Refund refund = Refund.builder()
                    .order(order)
                    .passenger(passenger)
                    .refundAmount(refundAmount)
                    .penaltyAmount(penaltyAmount)
                    .operationMode(request.getOperationMode() != null
                            ? request.getOperationMode().name()
                            : "voluntary")
                    .fullRefund(request.getFullRefund())
                    .pretend(request.getPretend())
                    .status(status)
                    .build();

            // Если не pretend — реально меняем статус заказа
            if (Boolean.FALSE.equals(request.getPretend())) {
                order.setStatus(OrderStatus.REFUNDED);
            }

            refundRepo.save(refund);

            passengerRefunds.add(
                    RefundQuoteResponse.PassengerRefund.builder()
                            .passengerId(passenger.getId())
                            .firstName(passenger.getFirstname())
                            .lastName(passenger.getLastname())
                            .refundAmount(refundAmount)
                            .penaltyAmount(penaltyAmount)
                            .build()
            );
        }

        if (Boolean.FALSE.equals(request.getPretend())) {
            orderRepo.save(order);
        }

        return RefundQuoteResponse.builder()
                .regnum(request.getRegnum())
                .status(status.toString())
                .operationMode(request.getOperationMode() != null
                        ? request.getOperationMode().name()
                        : "voluntary")
                .pretend(request.getPretend())
                .passengers(passengerRefunds)
                .totalRefundAmount(
                        passengerRefunds.stream()
                                .map(RefundQuoteResponse.PassengerRefund::getRefundAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .build();
    }
}