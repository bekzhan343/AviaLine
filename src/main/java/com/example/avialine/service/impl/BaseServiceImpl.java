package com.example.avialine.service.impl;

import com.example.avialine.dto.*;
import com.example.avialine.dto.response.FaqAnswerResponse;
import com.example.avialine.dto.response.GetFaqResponse;
import com.example.avialine.dto.response.InfoSubInfoResponse;
import com.example.avialine.dto.response.PopularDirectsResponse;
import com.example.avialine.exception.NoStoryMatchesException;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.*;
import com.example.avialine.repo.*;
import com.example.avialine.service.BaseService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BaseServiceImpl implements BaseService {

    private final CountryRepo countryRepo;
    private final DTOMapper dtoMapper;
    private final DocRepo docRepo;
    private final BannerRepo bannerRepo;
    private final RuleRepo ruleRepo;
    private final FaqRepo faqRepo;
    private final InfoPageRepo infoPageRepo;
    private final SubInfoRepo subInfoRepo;
    private final PopularDirectoryRepo popularDirectoryRepo;
    private final StoryRepo storyRepo;

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

    @Override
    public List<GetFaqResponse> getFaq() {

        List<Faq> faqs = faqRepo.findAll();
        List<GetFaqResponse> faqResponses = new ArrayList<>();

        for (Faq faq : faqs){
            faqResponses.add(dtoMapper.toGetFaqResponse(faq));
        }

        return faqResponses;
    }

    @Override
    public FaqAnswerResponse getFaqAnswerBySlug(@NonNull String slug) {

        Faq faq = faqRepo.findBySlug(slug);


        return dtoMapper.toFaqAnswerResponse(faq);
    }

    @Override
    public InfoSubInfoResponse getInfo() {


        List<InfoPage> infoPages = infoPageRepo.findAllWithSubInfos();


        List<InfoPageDTO> infoPageDTOS = new ArrayList<>();

        for (InfoPage infoPage : infoPages){

            List<SubInfoDTO> subInfoDTOS = infoPage.getSubInfos()
                    .stream()
                    .map(dtoMapper::toSubInfoDTO)
                    .toList();

            InfoPageDTO infoPageDTO = dtoMapper.toInfoPageDTO(infoPage);
            infoPageDTO.setSubInfos(subInfoDTOS);

            infoPageDTOS.add(infoPageDTO);
        }

       return new InfoSubInfoResponse(infoPageDTOS);
    }

    @Override
    public List<PopularDirectsResponse> getPopularDirects() {

        List<PopularDirectory> pdList = popularDirectoryRepo.findAll();

        List<PopularDirectsResponse> pdResponses = new ArrayList<>();

        for (PopularDirectory popularDirectory : pdList) {
            PopularDirectsResponse directsResponse = dtoMapper.toPopularDirectsResponse(popularDirectory);

            pdResponses.add(directsResponse);
        }

        return pdResponses;
    }

    @Override
    public List<StoryDTO> getStories() {

        List<StoryDTO> storyDTOS = new ArrayList<>();

        List<Story> stories = storyRepo.findAll();

        for (Story story : stories){
            StoryDTO response = dtoMapper.toStoryDTO(story);

            storyDTOS.add(response);
        }

        return storyDTOS;
    }

    @Override
    public StoryDTO getStoryById(Integer id) {
        Story story = storyRepo.findById(id)
                .orElseThrow(() -> new NoStoryMatchesException(ApiErrorMessage.NO_STORY_MATCHES_MESSAGE.getMessage(id)));

        return dtoMapper.toStoryDTO(story);
    }

    @Override
    public List<SubInfoDTO> getSubInfoBySlug(String slug) {

        InfoPage infoPage = infoPageRepo.findBySlug(slug);

        List<SubInfo> subInfo = infoPage.getSubInfos();

        return subInfo.stream()
                .map(dtoMapper::toSubInfoDTO)
                .toList();
    }
}
