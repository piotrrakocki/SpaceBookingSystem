package com.myproject.spacebooking.flight.model;

import com.myproject.spacebooking.rocket.model.Rocket;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String flightNumber;
    private String departurePlanet;
    private String destinationPlanet;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private int availableSeats;
    private double price;

    @Enumerated(EnumType.STRING)
    private FlightClass flightClass;

    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;

    @ManyToOne
    @JoinColumn(name = "rocket_id")
    private Rocket rocket;

    public Flight(String name, String flightNumber, String departurePlanet, String destinationPlanet, LocalDateTime departureDate, LocalDateTime arrivalDate, int availableSeats, double price, FlightClass flightClass, FlightStatus flightStatus, Rocket rocket) {
        this.name = name;
        this.flightNumber = flightNumber;
        this.departurePlanet = departurePlanet;
        this.destinationPlanet = destinationPlanet;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.availableSeats = availableSeats;
        this.price = price;
        this.flightClass = flightClass;
        this.flightStatus = flightStatus;
        this.rocket = rocket;
    }
}
