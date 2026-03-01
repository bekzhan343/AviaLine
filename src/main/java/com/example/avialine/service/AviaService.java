package com.example.avialine.service;

import com.example.avialine.dto.PrivacyPoliceDTO;
import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.dto.request.DepArrRequest;
import com.example.avialine.dto.request.SearchTicketRequest;
import com.example.avialine.dto.response.PNRResponse;
import com.example.avialine.dto.response.ScheduleResponse;
import com.example.avialine.dto.response.SearchParamsResponse;
import com.example.avialine.dto.response.SearchTicketResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public interface AviaService {

    Set<SearchParamsResponse> getCountryDetail();

    List<PrivacyPoliceDTO> getAllPrivacyPolices();

    String billPoints();
    String billStatic();

    SearchTicketResponse searchTicket(@NotNull SearchTicketRequest request);

    ScheduleResponse getSchedule(DepArrRequest request);

    PNRResponse booking(BookingRequest request);
}

