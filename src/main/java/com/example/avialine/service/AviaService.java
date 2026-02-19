package com.example.avialine.service;

import com.example.avialine.dto.PrivacyPoliceDTO;
import com.example.avialine.dto.response.SearchParamsResponse;

import java.util.List;
import java.util.Set;

public interface AviaService {

    Set<SearchParamsResponse> getCountryDetail();

    List<PrivacyPoliceDTO> getAllPrivacyPolices();

    String billPoints();
    String billStatic();
}

