package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class SearchTicketResponse implements Serializable {

    private final List<Variant> variants;

    @Data
    @Builder
    public static class Variant implements Serializable {

        @JsonProperty("flight_number")
        private String flightNumber;

        @JsonProperty("carrier")
        private String carrier;

        @JsonProperty("airplane")
        private String airplane;

        @JsonProperty("date")
        private LocalDate date;

        @JsonProperty("direct")
        private Boolean direct;

        @JsonProperty("departure")
        private DepartureArrival departure;

        @JsonProperty("arrival")
        private DepartureArrival arrival;




        @JsonProperty("flight_time")
        private String flightTime;

        @JsonProperty("meals")
        private String meals;

        @JsonProperty("variant_total")
        private BigDecimal variantTotal;

        @JsonProperty("available")
        private Integer available;

        @JsonProperty("bag_norm_full")
        private Integer bagNormFull;

        private List<PassengersPrice> passengers;

        private String currency;
    }

    @Data
    @Builder
    public static class DepartureArrival implements Serializable {

        @JsonProperty("code")
        private String code;

        @JsonProperty("time")
        private LocalTime time;
    }

    @Data
    @Builder
    public static class PassengersPrice implements Serializable {

        @JsonProperty("pax_code")
        private String paxCode;

        @JsonProperty("count")
        private Integer count;

        @JsonProperty("fare")
        private BigDecimal fare;

        @JsonProperty("taxes")
        private BigDecimal taxes;

        @JsonProperty("total")
        private BigDecimal total;
    }
}
