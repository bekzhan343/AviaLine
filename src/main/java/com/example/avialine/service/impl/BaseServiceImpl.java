package com.example.avialine.service.impl;

import com.example.avialine.dto.AvailDocsDTO;
import com.example.avialine.dto.BannerDTO;
import com.example.avialine.dto.CountryDTO;
import com.example.avialine.dto.RuleDTO;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.model.entity.Banner;
import com.example.avialine.model.entity.Country;
import com.example.avialine.model.entity.Doc;
import com.example.avialine.model.entity.Rule;
import com.example.avialine.repo.BannerRepo;
import com.example.avialine.repo.CountryRepo;
import com.example.avialine.repo.DocRepo;
import com.example.avialine.repo.RuleRepo;
import com.example.avialine.service.BaseService;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class BaseServiceImpl implements BaseService {

    private final CountryRepo countryRepo;
    private final DTOMapper dtoMapper;
    private final DocRepo docRepo;
    private final BannerRepo bannerRepo;
    private final RuleRepo ruleRepo;

    @Override
    public List<CountryDTO> getCountries() {

        List<CountryDTO> countryDTOList = new ArrayList<>();

        for (Country country : countryRepo.findAll()){
            CountryDTO dto = dtoMapper.toCountryDTO(country);

            countryDTOList.add(dto);
        }


        return countryDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailDocsDTO> getAvailDocs() {
        List<AvailDocsDTO> docsDTOS = new ArrayList<>();

        List<Doc> docs = docRepo.findAllWithCountries();

        for (Doc doc : docs){

            List<String> avail = doc.getCountries()
                    .stream()
                    .map(Country::getCode)
                    .toList();


            AvailDocsDTO dto = dtoMapper.toDocsDTO(doc,avail);

            docsDTOS.add(dto);
        }

        return docsDTOS;
    }

    @Override
    public List<BannerDTO> getBanners() {

        List<BannerDTO> bannerDTOS = new ArrayList<>();

        List<Banner> banners = bannerRepo.findAll();

        for (Banner b : banners){
            bannerDTOS.add(dtoMapper.toBannerDTO(b));
        }


        return bannerDTOS;
    }

    @Override
    public List<RuleDTO> getRules() {

        List<RuleDTO> ruleDTOS = new ArrayList<>();

        List<Rule> rules = ruleRepo.findAll();

        for (Rule rule : rules){
            ruleDTOS.add(dtoMapper.toRuleDTO(rule));
        }


        return ruleDTOS;
    }
}
