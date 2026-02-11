package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "language", length = 2, nullable = false)
    private String language;

    @Column(name = "img_pc", length = 500, nullable = false)
    private String imgPc;

    @Column(name = "img_mb", length = 500, nullable = false)
    private String imgMb;

    @Column(name = "title", length = 300, nullable = false)
    private String title;

    @Column(name = "detailed", nullable = false)
    private String detailed;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = Instant.now();
    }


}
