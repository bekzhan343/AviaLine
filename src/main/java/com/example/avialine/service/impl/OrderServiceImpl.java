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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    @Override
    public Order createOrder(Booking booking) {

        BigDecimal baseFare = booking.getBookingSegments().stream()
                .map(seg -> seg.getSchedule().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(booking.getPassengers().size()));



        Order order = Order
                .builder()
                .baseFare(baseFare)
                .booking(booking)
                .currency(booking.getCurrency())
                .status(OrderStatus.CREATED)
                .regnum(booking.getPnrNumber())
                .passengerCount(booking.getPassengers().size())
                .build();

        BigDecimal totalTax = baseFare.multiply(order.getTaxPercentage().divide(new BigDecimal(100), 2,  RoundingMode.HALF_UP));
        BigDecimal totalPrice = baseFare.add(totalTax);

        order.setTaxes(totalTax);
        order.setTotalAmount(totalPrice);
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

    @Override
    public Order getOrderByRegnum(String regnum) {
        return orderRepo.findOrderByRegnum(regnum)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.ORDER_NOT_FOUND_MESSAGE.getMessage()));
    }

    @Override
    public void markAsPendingAndSave(Order order) {
        order.setStatus(OrderStatus.PENDING);
        orderRepo.save(order);
    }


    @Override
    public void markAsPaidAndSave(Order order) {
        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);
    }

    @Override
    public void markAsCancelledAndSave(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }

    @Override
    public void markAsRefundAndSave(Order order) {
        order.setStatus(OrderStatus.REFUNDED);
        orderRepo.save(order);
    }
}
