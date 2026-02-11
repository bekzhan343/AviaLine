package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "popular_directories")
public class PopularDirectory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "language", nullable = false, length = 2)
    private String language;

    @ManyToOne
    @JoinColumn(name = "to_airport_id")
    private Airport airportTo;

    @ManyToOne
    @JoinColumn(name = "from_airport_id")
    private Airport airportFrom;

    @Column(name = "slug", length = 100, nullable = false)
    private String slug;

    @Column(name = "image", length = 500, nullable = false)
    private String image;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = Instant.now();
    }

}
