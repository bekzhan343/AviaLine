package com.example.avialine.controller;

import com.example.avialine.dto.*;
import com.example.avialine.dto.response.*;
import com.example.avialine.exception.NoStoryMatchesException;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.repo.SubInfoRepo;
import com.example.avialine.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("${end.point.base-faq}")
    public ResponseEntity<List<GetFaqResponse>> getFaq(){ return ResponseEntity.status(200).body(baseService.getFaq()); }

    @GetMapping("${end.point.base-faq-slug}")
    public ResponseEntity<FaqAnswerResponse> getFaqAnswerBySlag(@PathVariable("slug") String slug){
        return ResponseEntity.status(200).body(
                baseService.getFaqAnswerBySlug(slug)
        );
    }

    @GetMapping("${end.point.base-info}")
    public ResponseEntity<InfoSubInfoResponse> getInfoSubInfo(){
        return ResponseEntity.status(200).body(baseService.getInfo());
    }

    @GetMapping("${end.point.base-popular-directories}")
    private ResponseEntity<List<PopularDirectsResponse>> getPopularDirectories(){
        return ResponseEntity.status(200).body(baseService.getPopularDirects());
    }

    @GetMapping("${end.point.base-stories}")
    private ResponseEntity<List<StoryDTO>> getStories(){
        return ResponseEntity.status(200).body(baseService.getStories());
    }

    @GetMapping("${end.point.base-story-by-id}")
    private ResponseEntity<?> getStoryById(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.status(200).body(baseService.getStoryById(id));
        }catch (NoStoryMatchesException e){
            return ResponseEntity.status(404).body(
                    new DetailErrorResponse(e.getMessage())
            );
        }
    }

    @GetMapping("${end.point.base-sub-info}")
    public ResponseEntity<?> getSubInfoBySlug(@RequestParam("slug") String slug){
        try {
            return ResponseEntity.status(200).body(baseService.getSubInfoBySlug(slug));
        }catch (NullPointerException e){
            return ResponseEntity.status(404).body(
                    new DetailErrorResponse(ApiErrorMessage.INFO_NOT_FOUND_MESSAGE.getMessage())
            );
        }
    }
}
