package com.example.avialine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingInfoResponse implements Serializable {

    private String surname;
    private String regnum;

    private String email;
    private String phone;
    private String currency;
    private List<SegmentsDetail> segments;
    private List<PassengersDetail> passengers;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class SegmentsDetail implements Serializable {

        private String company;
        private String flight;
        private String departure;
        private String arrival;
        private String date;
        private String subclass;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class PassengersDetail implements Serializable {

        private String lastname;
        private String firstname;
        private String surname;
        private String category;
        private String sex;
        private String birthdate;
    }
}
