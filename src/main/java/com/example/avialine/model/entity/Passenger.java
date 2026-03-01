package com.example.avialine.model.entity;

import com.example.avialine.enums.Category;
import com.example.avialine.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "firstname", nullable = false, length = 100)
    private String firstname;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 3)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", length = 10)
    private Sex sex;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "doc_country", length = 2, nullable = false)
    private String docCountry;

    @Column(name = "doc_Code", length = 55, nullable = false)
    private String docCode;

    @Column(name = "doc", length = 3, nullable = false)
    private String doc;

    @Column(name = "pspexpire", nullable = false)
    private LocalDate pspexpire;

}
