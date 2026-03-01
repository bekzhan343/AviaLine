package com.example.avialine.service.impl;

import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.enums.Category;
import com.example.avialine.enums.Sex;
import com.example.avialine.exception.ValidationException;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.BookingSegment;
import com.example.avialine.model.entity.Passenger;
import com.example.avialine.service.PassengerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PassengerServiceImpl implements PassengerService {


    @Override
    public List<Passenger> createPassenger(Booking booking, BookingRequest request, List<BookingSegment> bookingSegments) {

        Map<String, List<String>> errors = new HashMap<>();
        List<Passenger> passengers = new ArrayList<>();
        List<BookingRequest.BookingPassenger> requestPassengers = request.getPassengers();

        for (BookingRequest.BookingPassenger bPassenger : requestPassengers) {

            Category category = Category.valueOf(bPassenger.getCategory());

            LocalDate date = request.getSegments().getLast().getDate();
            int age = Period.between(bPassenger.getBirthDate(), date).getYears();

            switch (category){
                case INF -> {
                    if (age >= 2) {
                        errors.put("Invalid age!", List.of(ApiErrorMessage.INF_AGE_ERROR_MESSAGE.getMessage()));
                        throw new ValidationException("Error:", errors); }
                }
                case CHD -> {
                    if (age < 2 || age >= 12) {
                        errors.put("Invalid age!", List.of(ApiErrorMessage.CHD_AGE_ERROR_MESSAGE.getMessage()));
                        throw new ValidationException("Error:", errors); }
                }

                case ADT -> {
                    if (age < 12) {
                        errors.put("Invalid age!", List.of(ApiErrorMessage.ADT_AGE_ERROR_MESSAGE.getMessage()));
                        throw new ValidationException("Error:", errors);
                    }
                }

                default -> {
                    errors.put("Unknown category: ", List.of(bPassenger.getCategory()));
                    throw new ValidationException("Error:", errors);
                }
            }

            if (bPassenger.getPspexpire().isBefore(bookingSegments.getLast().getDate().plusMonths(6))){
                errors.put("Invalid passport expiry!", List.of(ApiErrorMessage.PSP_EXPIRE_ERROR_MESSAGE.getMessage()));
                throw new ValidationException("error:", errors);
            }

            passengers.add(Passenger
                    .builder()
                    .booking(booking)
                    .lastname(bPassenger.getLastName())
                    .firstname(bPassenger.getFirstName())
                    .surname(bPassenger.getSurname())
                    .category(category)
                    .sex(Sex.valueOf(bPassenger.getSex()))
                    .birthdate(bPassenger.getBirthDate())
                    .docCountry(bPassenger.getDocCountry())
                    .doc(bPassenger.getDoc())
                    .docCode(bPassenger.getDocCode())
                    .pspexpire(bPassenger.getPspexpire())
                    .build()
            );
        }

        long infCount = passengers.stream().filter(p -> p.getCategory().equals(Category.INF)).count();
        long adtCount = passengers.stream().filter(p -> p.getCategory().equals(Category.ADT)).count();

        if (infCount > adtCount) {
            errors.put("Invalid passengers:", List.of(ApiErrorMessage.INVALID_INF_ADT_COMBINATION_MESSAGE.getMessage()));
            throw new ValidationException("error:", errors);
        }

        return passengers;
    }
}
