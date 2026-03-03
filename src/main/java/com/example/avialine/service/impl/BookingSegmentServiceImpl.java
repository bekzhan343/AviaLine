package com.example.avialine.service.impl;

import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.BookingSegment;
import com.example.avialine.model.entity.Flight;
import com.example.avialine.model.entity.FlightSchedule;
import com.example.avialine.repo.BookingSegmentRepo;
import com.example.avialine.repo.FlightRepo;
import com.example.avialine.repo.FlightScheduleRepo;
import com.example.avialine.service.BookingSegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BookingSegmentServiceImpl implements BookingSegmentService {

    private final FlightRepo flightRepo;
    private final FlightScheduleRepo flightScheduleRepo;
    private final BookingSegmentRepo segmentRepo;

    @Override
    public List<BookingSegment> createBookingSegments(Booking booking, List<BookingRequest.Segment> segments) {

        List<BookingSegment> bookingSegments = new ArrayList<>();

        for (BookingRequest.Segment seg : segments) {
            Flight flight = flightRepo.findFlightByParameters(
                    seg.getCompany(),
                    seg.getFlight(),
                    seg.getDeparture(),
                    seg.getArrival()
            ).orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.NO_SUITABLE_FLIGHT_FOUND_MESSAGE.getMessage()));

            FlightSchedule schedule = flightScheduleRepo.findByDateAndFlight(seg.getDate(), flight)
                            .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage
                                    .NO_SUITABLE_FLIGHT_FOUND_MESSAGE
                                    .getMessage()));

            bookingSegments.add(BookingSegment
                    .builder()
                    .booking(booking)
                    .company(flight.getCarrier())
                    .flight(flight.getFlightNumber())
                    .departure(flight.getDepartureCode())
                    .arrival(flight.getArrivalCode())
                    .date(schedule.getDate())
                    .subclass(seg.getSubclass())
                    .schedule(schedule)
                    .build()
            );
        }
        return bookingSegments;
    }

    @Override
    public List<BookingSegment> getBookingSegments(List<Booking> bookings) {

        List<BookingSegment> bookingSegments = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingSegment segment = segmentRepo.getByBookingId(booking.getId());

            bookingSegments.add(segment);
        }

        return bookingSegments;
    }


}
