package com.example.avialine.service;

import com.example.avialine.dto.AvailDocsDTO;
import com.example.avialine.dto.BannerDTO;
import com.example.avialine.dto.CountryDTO;
import com.example.avialine.dto.RuleDTO;


import java.util.List;

public interface BaseService {

    List<CountryDTO> getCountries();

    List<AvailDocsDTO> getAvailDocs();

    List<BannerDTO> getBanners();

    List<RuleDTO> getRules();
}
