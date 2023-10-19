package com.myproject.spacebooking.rocket.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Rocket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String manufacturer;
    private String capacity;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    public Rocket(String name, String manufacturer, String capacity, String description) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.capacity = capacity;
        this.description = description;
    }
}
