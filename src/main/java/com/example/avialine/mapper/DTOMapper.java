package com.example.avialine.mapper;

import com.example.avialine.dto.*;
import com.example.avialine.dto.response.FaqAnswerResponse;
import com.example.avialine.dto.response.GetFaqResponse;
import com.example.avialine.dto.PopularDirectDTO;
import com.example.avialine.dto.response.PopularDirectDetailResponse;
import com.example.avialine.model.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DTOMapper {


    public UserDTO toUserDTO(User user){
        return UserDTO
                .builder()
                .firstName(user.getName())
                .email(user.getEmail())
                .build();
    }

    public RoleDTO toRoleDTO(Role role){
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public UserProfileDTO toUserProfileDTO(User user,String accessToken){
        return UserProfileDTO
                .builder()
                .userId(user.getId())
                .token(accessToken)
                .phone(user.getPhone())
                .build();
    }

    public CountryDTO toCountryDTO(Country country){
        return CountryDTO
                .builder()
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    public AvailDocsDTO toDocsDTO(Doc doc, List<String> countryDTOS){
        return AvailDocsDTO.
                builder()
                .code(doc.getCode())
                .name(doc.getName())
                .avail(countryDTOS)
                .build();
    }

    public BannerDTO toBannerDTO(Banner banner){
        return BannerDTO
                .builder()
                .language(banner.getLanguage())
                .image(banner.getImage())
                .imageMobile(banner.getImageMobile())
                .title(banner.getTitle())
                .slug(banner.getSlug())
                .description(banner.getDescription())
                .build();
    }

    public RuleDTO toRuleDTO(Rule rule){
        return RuleDTO
                .builder()
                .language(rule.getLanguage())
                .slug(rule.getSlug())
                .title(rule.getTitle())
                .description(rule.getDescription())
                .build();
    }

    public GetFaqResponse toGetFaqResponse(Faq faq){
        return GetFaqResponse.builder()
                .language(faq.getLanguage())
                .slug(faq.getSlug())
                .question(faq.getQuestion())
                .build();
    }

    public FaqAnswerResponse toFaqAnswerResponse(Faq faq){
        return FaqAnswerResponse
                .builder()
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .build();
    }

    public InfoPageDTO toInfoPageDTO(InfoPage infoPage){
        return InfoPageDTO
                .builder()
                .language(infoPage.getLanguage())
                .slug(infoPage.getSlug())
                .title(infoPage.getTitle())
                .description(infoPage.getDescription())
                .backgroundColor(infoPage.getBackgroundColor())
                .image(infoPage.getImage())
                .build();
    }

    public SubInfoDTO toSubInfoDTO(SubInfo subInfo) {
        return SubInfoDTO
                .builder()
                .title(subInfo.getTitle())
                .slug(subInfo.getSlug())
                .description(subInfo.getDescription())
                .subject(subInfo.getSubject())
                .build();
    }

    public PopularDirectDTO toPopularDirectsResponse(PopularDirectory popularDirectory){
        return PopularDirectDTO
                .builder()
                .id(popularDirectory.getId())
                .name(popularDirectory.getName())
                .description(popularDirectory.getDescription())
                .codeTo(popularDirectory.getAirportTo().getCode())
                .codeFrom(popularDirectory.getAirportFrom().getCode())
                .slug(popularDirectory.getSlug())
                .language(popularDirectory.getLanguage())
                .image(popularDirectory.getImage())
                .build();
    }

    public StoryDTO toStoryDTO(Story story){
        return StoryDTO
                .builder()
                .id(story.getId())
                .language(story.getLanguage())
                .imgPc(story.getImgPc())
                .imgMB(story.getImgMb())
                .title(story.getTitle())
                .detailed(story.getDetailed())
                .description(story.getDescription())
                .build();
    }

    public PopularDirectDetailResponse toPopularDirectDetailResponse(PopularDirectory popularDirectory){
        return PopularDirectDetailResponse
                .builder()
                .id(popularDirectory.getId())
                .description(popularDirectory.getDescription())
                .image(popularDirectory.getImage())
                .build();
    }


}
