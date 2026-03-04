package com.example.avialine.service.impl;

import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.enums.OrderStatus;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.Order;
import com.example.avialine.repo.OrderRepo;
import com.example.avialine.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    @Override
    public Order createOrder(Booking booking) {

        BigDecimal totalPrice = booking.getBookingSegments().stream()
                .map(seg -> seg.getSchedule().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(booking.getPassengers().size()));

        Order order = Order
                .builder()
                .price(totalPrice)
                .booking(booking)
                .currency(booking.getCurrency())
                .status(OrderStatus.CREATED)
                .regnum(booking.getPnrNumber())
                .passengerCount(booking.getPassengers().size())
                .build();

        return orderRepo.save(order);
    }

    @Override
    public List<Order> getAllOrders(List<Booking> bookings) {

        List<Order> orders = new ArrayList<>();

        for (Booking booking : bookings) {
            Order order = orderRepo.findByBookingId(booking.getId())
                    .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.ORDER_NOT_FOUND_MESSAGE.getMessage()));

            orders.add(order);
        }

        return orders;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.ORDER_NOT_FOUND_MESSAGE.getMessage()));
    }


}
