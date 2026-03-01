package com.example.avialine.service;

import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.BookingSegment;
import com.example.avialine.model.entity.Passenger;

import java.util.List;

public interface PassengerService {

    List<Passenger> createPassenger(Booking booking, BookingRequest request, List<BookingSegment> bookingSegments);
}
