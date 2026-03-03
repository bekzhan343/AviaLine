package com.example.avialine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDTO implements Serializable {

    @JsonProperty("order_id")
    private Integer orderId;

    private String regnum;

    private String email;

    private String status;

    private String price;

    private String currency;

    private List<SegmentShort> segments;

    private List<PassengerShort> passengers;

    @JsonProperty("passengers_count")
    private Integer passengersCount;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SegmentShort implements Serializable {

        private String flight;

        private String company;

        private String duration;

        private String departure;

        private String arrival;

        @JsonProperty("departure_date")
        private String departureDate;

        @JsonProperty("departure_time")
        private String departureTime;

        @JsonProperty("arrival_date")
        private String arrivalDate;

        @JsonProperty("arrival_time")
        private String arrivalTime;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PassengerShort implements Serializable {

        private Integer id;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        private String surname;

        private String category;

        private String sex;

        private String birthdate;

        @JsonProperty("doc_country")
        private String docCountry;

        private String doc;

        private String pspexpire;

        @JsonProperty("doc_code")
        private String docCode;
    }
}
