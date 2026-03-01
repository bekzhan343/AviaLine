package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse implements Serializable {

    private Integer count;

    @JsonProperty("next")
    private String nextPagePath;

    @JsonProperty("previous")
    private String previousPagePath;

    private Integer current;

    private List<Result> results;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result implements Serializable {

        String company;

        String num;

        @JsonProperty("operating_company")
        String operatingCompany;

        @JsonProperty("operating_flight")
        String operatingFlight;

        String origin; // departure

        String destination; // arrival

        @JsonProperty("depttime")
        String departureTime;

        @JsonProperty("arrvtime")
        String arrivalTime;

        @JsonProperty("flightTime")
        String flightTime;

        String airplane;

        @JsonProperty("service_type")
        String serviceType;

        @JsonProperty("orig_term")
        String origTerm;

        @JsonProperty("dest_term")
        String destTerm;

        @JsonProperty("origin_type")
        String originType;

        @JsonProperty("origin_city")
        String originCity;

        @JsonProperty("destination_type")
        String destinationType;

        @JsonProperty("destination_city")
        String destinationCity;

        @JsonProperty("classes")
        Class classes;

    }

    @Builder
    @Data
    public static class Class implements Serializable{
        Boolean econom;

        Boolean business;

        Boolean first;
    }
}
