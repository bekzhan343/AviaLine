package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flight_schedule")
public class FlightSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "available")
    private Integer available;

    @Column(name = "direct")
    private Boolean direct;

    @Column(name = "avail_econom")
    private Boolean availEconom;

    @Column(name = "avail_Business")
    private Boolean availBusiness;

    @Column(name = "avail_first")
    private Boolean availFirst;

}
