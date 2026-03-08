package com.example.avialine.dto.response;

import com.example.avialine.enums.OrderStatus;
import com.example.avialine.enums.PaxCode;
import com.example.avialine.enums.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageBookingResponse implements Serializable {

    private String regnum;

    private Set<ManageSegments> segments;

    private Set<ManagePassenger> passengers;

    private Price price;

    private Contact contact;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ManageSegments implements Serializable {

        private String company;

        private Integer flight;

        private String departure;

        private String arrival;

        private LocalDate date;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ManagePassenger implements Serializable {

        @JsonProperty("full_name")
        private String fullName;

        private String category;

        private Sex sex;

        private LocalDate birthdate;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Contact implements Serializable {
        @JsonProperty("email")
        private String email;

        private String phone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Price implements Serializable {

        @JsonProperty("total_price")
        private BigDecimal totalPrice;

        private OrderStatus status;
    }

}
