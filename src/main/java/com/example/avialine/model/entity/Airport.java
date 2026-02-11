package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airports",
        uniqueConstraints = {})
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "code", nullable = false, length = 3, unique = true)
    private String code;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @ManyToOne
    @JoinColumn(
            name = "country_id",
            nullable = false
    )
    private Country country;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = Instant.now();
    }

    @OneToMany(mappedBy = "airportTo")
    private List<PopularDirectory> departures;

    @OneToMany(mappedBy = "airportFrom")
    private List<PopularDirectory> arrivals;
}
