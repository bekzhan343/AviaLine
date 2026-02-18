package com.example.avialine.service.impl;

import com.example.avialine.dto.AirportDTO;
import com.example.avialine.dto.CityDTO;
import com.example.avialine.dto.PrivacyPoliceDTO;
import com.example.avialine.dto.response.SearchParamsResponse;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.model.entity.Country;
import com.example.avialine.model.entity.PrivacyPolice;
import com.example.avialine.repo.CountryRepo;
import com.example.avialine.repo.PrivacyPoliceRepo;
import com.example.avialine.service.AviaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AviaServiceImpl implements AviaService {

    private final CountryRepo countryRepo;
    private final PrivacyPoliceRepo privacyPoliceRepo;
    private final DTOMapper dtoMapper;


    @Override
    public Set<SearchParamsResponse> getCountryDetail() {

        List<Country> countries = countryRepo.findCountryWithAirports();

        return countries.stream().map(
                country -> {
                    List<CityDTO> cityDTOS = country.getCities().stream().map(
                            city -> {
                                List<AirportDTO> airportDTOS = city.getAirports().stream().map(
                                        airport -> {
                                            return new AirportDTO(
                                                    airport.getId(),
                                                    airport.getName(),
                                                    airport.getCode()
                                            );
                                        }
                                        ).toList();

                                return new CityDTO(
                                        city.getId(),
                                        city.getName(),
                                        city.getCode(),
                                        airportDTOS
                                );
                            }
                    ).toList();

                    return SearchParamsResponse
                            .builder()
                            .id(country.getId())
                            .name(country.getName())
                            .codeName(country.getCode())
                            .image(country.getImage())
                            .cities(cityDTOS)
                            .build();
                }
        ).collect(Collectors.toSet());
    }

    @Override
    public List<PrivacyPoliceDTO> getAllPrivacyPolices() {
        return privacyPoliceRepo
                .findAll()
                .stream()
                .map(dtoMapper::toPrivacyPoliceDTO)
                .toList();
    }
}
