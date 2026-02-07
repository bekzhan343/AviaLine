package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "banners")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "language", nullable = false, length = 10)
    private String language;

    @Column(name = "image", nullable = false, length = 100)
    private String image;

    @Column(name = "image_mobile", nullable = false, length = 100)
    private String imageMobile;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    @Column(name = "description")
    private String description;

}
