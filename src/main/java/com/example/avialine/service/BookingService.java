package com.example.avialine.service;


import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.User;


public interface BookingService {

    Booking createBooking(BookingRequest request, User user);
}
