package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "info_page",
        uniqueConstraints = {
                @UniqueConstraint(name = "info_slug_lang_unique",
                columnNames = {"slug", "language"})
        }


)
public class InfoPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image", nullable = false, length = 500)
    private String image;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "background_color", nullable = false, length = 500)
    private String backgroundColor;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "infoPage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubInfo> subInfos;

}
