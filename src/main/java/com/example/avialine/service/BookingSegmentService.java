package com.example.avialine.service;

import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.BookingSegment;

import java.util.List;
import java.util.Set;

public interface BookingSegmentService {

    List<BookingSegment> createBookingSegments(Booking booking, List<BookingRequest.Segment> segments);

    List<BookingSegment> getBookingSegments(List<Booking> bookings);
}
