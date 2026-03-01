package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking_segments")
public class BookingSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "company", length = 50, nullable = false)
    private String company;

    @Column(name = "flight", length = 10, nullable = false)
    private String flight;

    @Column(name = "departure", length = 3, nullable = false)
    private String departure;

    @Column(name = "arrival", length = 3, nullable = false)
    private String arrival;

    @Column(name = "set_date", nullable = false)
    private LocalDate date;

    @Column(name = "subclass")
    private String subclass;
}
