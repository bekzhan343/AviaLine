package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "flight_number", length = 10)
    private String flightNumber;

    @Column(name = "carrier", length = 5)
    private String carrier;

    @Column(name = "departure_code", length = 3)
    private String departureCode;

    @Column(name = "arrival_code", length = 3)
    private String arrivalCode;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "flight_minutes")
    private Integer flightMinutes;

    @Column(name = "airplane", length = 20)
    private String airplane;

    @Column(name = "meals", length = 5)
    private String meals;

    @Column(name = "bag_norm_kg")
    private Integer bagNormKg;

    @Column(name = "service_type", length = 2)
    private String serviceType;

    @Column(name = "origin_terminal", length = 10)
    private String originTerminal;

    @Column(name = "destination_terminal", length = 10)
    private String destinationTerminal;



}
