package com.example.avialine.service;

import com.example.avialine.dto.OrderDTO;
import com.example.avialine.dto.PrivacyPoliceDTO;
import com.example.avialine.dto.request.*;
import com.example.avialine.dto.response.*;
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

    BookingInfoResponse detailBooking(@NotNull RegnumSurnameRequest request);

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(@NotNull Integer orderId);

    OrderStatusResponse getOrderStatus(@NotNull String regnum);

    PNRResponse addInfant(AddInfantRequest request);
}

