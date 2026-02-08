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
@Table(name = "subinfo")
public class SubInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "info_page_id", nullable = false)
    private InfoPage infoPage;

    @Column(name = "title", length = 300, nullable = false)
    private String title;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "subject",length = 300, nullable = false)
    private String subject;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt = null;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

}
