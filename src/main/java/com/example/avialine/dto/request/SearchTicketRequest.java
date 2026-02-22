package com.example.avialine.dto.request;

import com.example.avialine.enums.Currency;
import com.example.avialine.enums.PaxCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchTicketRequest implements Serializable {

    private List<Segments> segments;

    private List<Passenger> passengers;

    @Schema(example = "USD")
    private Currency currency;

    private AnswerParams answerParams;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Segments implements Serializable{

        @Schema(example = "BSZ")
        @Length(min = 3, max = 3)
        private String departure;

        @Schema(example = "MSC")
        @Length(min = 3, max = 3)
        private String arrival;

        @Schema(example = "2026-02-19")
        private LocalDate date;

        @Schema(example = "true")
        private Boolean direct;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Passenger implements Serializable{

        @Schema(example = "ADT(adult)")
        private PaxCode code;

        @Schema(example = "1")
        private Integer count;

        @Schema(example = "13")
        private Integer age;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class AnswerParams implements Serializable{

        @JsonProperty("show_flighttime")
        private Boolean showFlightTime;

        @JsonProperty("show_meals")
        private Boolean showMeals;

        @JsonProperty("show_varianttotal")
        private Boolean showVariantTotal;

        @JsonProperty("show_available")
        private Boolean showAvailable;

        @JsonProperty("show_bag_norm_full")
        private Boolean showBagNormalFull;
    }
}
