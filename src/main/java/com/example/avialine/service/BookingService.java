package com.example.avialine.service;


import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;


public interface BookingService {

    Booking createBooking(BookingRequest request, User user);

    Booking getBookingBySurnameAndPnr(String surname, String pnrNumber);

    List<Booking> getByUser(User user);

    void markAsCancelledAndSave(@NotNull Booking booking);

    void markAsRefundAndSave(@NotNull Booking booking);
}
