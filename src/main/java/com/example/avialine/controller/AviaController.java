package com.example.avialine.controller;

import com.example.avialine.dto.PrivacyPoliceDTO;
import com.example.avialine.dto.response.SearchParamsResponse;
import com.example.avialine.service.AviaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                aviaService.billPoints()
        );
    }
}
