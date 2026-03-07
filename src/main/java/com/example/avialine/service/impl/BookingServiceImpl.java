package com.example.avialine.service.impl;

import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.enums.ApiErrorMessage;
import com.example.avialine.enums.BookingStatus;
import com.example.avialine.enums.Currency;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.model.entity.Booking;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.BookingRepo;
import com.example.avialine.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final SecureRandom random = new SecureRandom();

    @Override
    public Booking createBooking(BookingRequest request, User user) {

        String pnr;

        int attempts = 0;

        do {
            if (attempts++ > 10) { throw new RuntimeException(ApiErrorMessage.FAILED_TO_GENERATE_PNR_MESSAGE.getMessage()); }
                pnr = generatePnr();
        }while (bookingRepo.existsByPnrNumber(pnr));

        return Booking
                .builder()
                .status(BookingStatus.CREATED)
                .phoneNumber(request.getPhone())
                .email(request.getEmail())
                .currency(Currency.valueOf(request.getCurrency()))
                .updatedAt(null)
                .pnrVersion(1)
                .pnrNumber(pnr)
                .user(user)
                .build();
    }

    @Override
    public Booking getBookingBySurnameAndPnr(String surname, String pnrNumber) {
        return bookingRepo.findByPnrNumberAndSurname(pnrNumber, surname)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.BOOKING_NOT_FOUND_BY_PNR_AND_SURNAME_MESSAGE.getMessage()));
    }

    @Override
    public List<Booking> getByUser(User user) {

        return bookingRepo.getByUser(user);
    }

    private String generatePnr(){
        String chars = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 6; i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
