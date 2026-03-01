package com.example.avialine.dto.request;

import com.example.avialine.security.util.DateDeserializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingRequest implements Serializable {

    @NotEmpty(message = "Segments list cannot be empty")
    @Valid
    private List<Segment> segments;

    @NotEmpty(message = "Passengers list cannot be empty")
    @Valid
    private List<BookingPassenger> passengers;

    @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Pattern(regexp = "^\\+996\\d{9}$",
            message = "Invalid phone number! Expected format: +996XXXXXXXXX"
    )
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "KGZ|KZT|USD|RUB", message = "Currency must be one of: KGZ, KZT, USD, RUB")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 chars!")
    private String currency;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Segment implements Serializable {

        @NotBlank(message = "Company is required")
        @Size(max = 50, message = "Company must not exceed 50 characters")
        private String company;

        @NotBlank(message = "Flight is required")
        @Size(max = 10, message = "Flight must not exceed 10 characters")
        private String flight;

        @NotBlank(message = "Departure is required")
        @Size(min = 3, max = 3, message = "Departure must be exactly 3 characters (IATA code)")
        @Pattern(regexp = "BSZ|MSC|LED|OSS|SVX")
        private String departure;

        @NotBlank(message = "Arrival is required")
        @Size(min = 3, max = 3, message = "Arrival must be exactly 3 characters (IATA code)")
        @Pattern(regexp = "MSC|BSZ|LED|OSS|SVX|IKT")
        private String arrival;

        @NotNull(message = "Date cannot be empty or null!")
        @JsonDeserialize(using = DateDeserializer.class)
        @FutureOrPresent(message = "Date cannot be in past!")
        private LocalDate date;

        @Size(max = 5, message = "Subclass must not exceed 5 characters")
        private String subclass;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class BookingPassenger implements Serializable {

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        private String lastName;

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        private String firstName;

        @Size(max = 100, message = "Surname must not exceed 100 characters")
        private String surname; // nullable (middlename)

        @NotBlank(message = "Category is required")
        @Pattern(regexp = "ADT|INF|CHD", message = "Category must be one of: ADT, INF, CHD")
        private String category;

        @NotBlank(message = "Sex is required")
        @Pattern(regexp = "MALE|FEMALE", message = "Sex must be MALE or FEMALE")
        private String sex;

        @NotNull(message = "Birthdate cannot be empty or null!")
        @JsonDeserialize(using = DateDeserializer.class)
        @Past(message = "Birthdate must be in the past")
        private LocalDate birthDate;

        @NotBlank(message = "Document country is required")
        @Size(min = 2, max = 2, message = "Document country must be exactly 2 characters (ISO code)")
        private String docCountry;

        @NotBlank(message = "Document code is required")
        @Size(max = 3, message = "Document code must not exceed 3 characters")
        private String docCode;

        @NotBlank(message = "Document number is required")
        @Size(min = 2, max = 3, message = "Doc must be exactly2 or 3 chars")
        private String doc;

        @NotNull(message = "pspexpire cannot be empty or null!")
        @JsonDeserialize(using = DateDeserializer.class)
        @Future(message = "Passport must not be expired")
        private LocalDate pspexpire;
    }
}