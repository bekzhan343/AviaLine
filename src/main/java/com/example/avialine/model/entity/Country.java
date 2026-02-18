package com.example.avialine.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", length = 10, unique = true, nullable = false)
    private String code;

    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    @Column(name = "image", length = 500)
    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "docs_avail_country",
            joinColumns = @JoinColumn(name = "country_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    private Set<Doc> docs;



    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private Set<City> cities;
}
