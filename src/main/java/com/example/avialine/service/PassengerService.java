package com.example.avialine.service;

import com.example.avialine.dto.request.AddInfantRequest;
import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.BookingSegment;
import com.example.avialine.model.entity.Passenger;

import java.util.List;
import java.util.Set;

public interface PassengerService {

    List<Passenger> createPassenger(Booking booking, BookingRequest request, List<BookingSegment> bookingSegments);

    List<Passenger> getPassengers(List<Booking> booking);

    Passenger addInfant(AddInfantRequest request, Booking booking, String docCountry);

    Passenger getPassengerById(Integer id);
}
