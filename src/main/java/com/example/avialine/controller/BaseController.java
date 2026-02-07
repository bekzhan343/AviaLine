package com.example.avialine.controller;

import com.example.avialine.dto.AvailDocsDTO;
import com.example.avialine.dto.BannerDTO;
import com.example.avialine.dto.CountryDTO;
import com.example.avialine.dto.RuleDTO;
import com.example.avialine.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("${end.point.base}")
@RequiredArgsConstructor
@RestController
public class BaseController {

    private final BaseService baseService;

    @GetMapping("${end.point.base-countries}")
    public ResponseEntity<List<CountryDTO>> getCountries() {
        return ResponseEntity.status(200).body(baseService.getCountries());
    }

    @GetMapping("${end.point.base-docs}")
    public ResponseEntity<List<AvailDocsDTO>> getAvailDocs(){
        return ResponseEntity.status(200).body(baseService.getAvailDocs());
    }

    @GetMapping("${end.point.base-banners}")
    public ResponseEntity<List<BannerDTO>> getBanners() {
        return ResponseEntity
                .status(200)
                .body(baseService.getBanners());
    }

    @GetMapping("${end.point.base-rules}")
    public ResponseEntity<List<RuleDTO>> getRoles() {
        return ResponseEntity.status(200).body(baseService.getRules());
    }

}
