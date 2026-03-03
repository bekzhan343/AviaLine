package com.example.avialine.service;

import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.Order;

import java.util.List;


public interface OrderService {

    Order createOrder(Booking booking);

    List<Order> getAllOrders(List<Booking> bookings);
}
