package com.example.avialine.model.entity;

import com.example.avialine.enums.Currency;
import com.example.avialine.enums.PaxCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(name = "pax_code", length = 5)
    @Enumerated(EnumType.STRING)
    private PaxCode paxCode;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(name = "fare")
    private BigDecimal fare;


    @Column(name = "taxes")
    private BigDecimal taxes;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
