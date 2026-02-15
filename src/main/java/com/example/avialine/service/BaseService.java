package com.example.avialine.service;

import com.example.avialine.dto.*;
import com.example.avialine.dto.response.*;


import java.util.List;

public interface BaseService {

    List<CountryDTO> getCountries();

    List<AvailDocsDTO> getAvailDocs();

    List<BannerDTO> getBanners();

    List<RuleDTO> getRules();

    List<GetFaqResponse> getFaq();

    FaqAnswerResponse getFaqAnswerBySlug(String slug);

    InfoSubInfoResponse getInfo();

    List<PopularDirectDTO> getPopularDirects();

    List<StoryDTO> getStories();

    StoryDTO getStoryById(Integer id);

    List<SubInfoDTO> getSubInfoBySlug(String slug);

    PopularDirectDetailResponse getPopularDirectById(Integer id);

    List<SendServiceResponse> getServices();
}
