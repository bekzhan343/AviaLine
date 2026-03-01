package com.example.avialine.controller;

import com.example.avialine.dto.PrivacyPoliceDTO;
import com.example.avialine.dto.request.BookingRequest;
import com.example.avialine.dto.request.DepArrRequest;
import com.example.avialine.dto.request.SearchTicketRequest;
import com.example.avialine.dto.response.DetailErrorResponse;
import com.example.avialine.dto.response.SearchParamsResponse;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.exception.PastDateException;
import com.example.avialine.service.AviaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("${end.point.avia-base}")
@RestController
public class AviaController {

    private final AviaService aviaService;

    @GetMapping("${end.point.avia-search-params}")
    public ResponseEntity<Set<SearchParamsResponse>> getSearchParams(){
        return ResponseEntity.status(200).body(aviaService.getCountryDetail());
    }

    @GetMapping("${end.point.avia-privacy-policy}")
    public ResponseEntity<List<PrivacyPoliceDTO>> getAttPrivacyPolice(){
        return ResponseEntity.ok(
                aviaService.getAllPrivacyPolices()
        );
    }

    @GetMapping("${end.point.avia-sirena-bill-points}")
    public ResponseEntity<String> billPoints(){
        return ResponseEntity.status(200).body(
                aviaService.billPoints()
        );
    }

    @GetMapping("${end.point.avia-sirena-bill-static}")
    public ResponseEntity<String> billStatic(){
        return ResponseEntity.status(200).body(
                aviaService.billStatic()
        );
    }

    @PostMapping("${end.point.avia-search-ticket}")
    public ResponseEntity<?> getSearchTicket(@RequestBody SearchTicketRequest searchTicketRequest){
        try {
            return ResponseEntity.status(200).body(
                    aviaService.searchTicket(searchTicketRequest));
        }catch (IllegalArgumentException | PastDateException e){
            return ResponseEntity.status(400).body(
                    new DetailErrorResponse(
                            e.getMessage()
                    )
            );
        }
    }

    @PostMapping("${end.point.avia-schedule}")
    public ResponseEntity<?> getSchedule(@RequestBody DepArrRequest request){
        return ResponseEntity.status(200).body(
                aviaService.getSchedule(request)
        );
    }

    @PostMapping("${end.point.avia-booking}")
    public  ResponseEntity<?> getBooking(@Valid @RequestBody BookingRequest request){
        try {
            return ResponseEntity.status(200).body(
                    aviaService.booking(request)
            );
        }catch (DataNotFoundException e){
            return ResponseEntity.status(400).body(
                    new DetailErrorResponse(e.getMessage())
            );
        }
    }
}
