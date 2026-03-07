package com.example.avialine.service;


import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.User;

import java.util.List;


public interface BookingService {

    Booking createBooking(BookingRequest request, User user);

    Booking getBookingBySurnameAndPnr(String surname, String pnrNumber);

    List<Booking> getByUser(User user);

    Booking getBookingByRegnum(String pnr);
}
