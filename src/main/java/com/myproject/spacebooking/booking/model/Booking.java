package com.myproject.spacebooking.booking.model;

import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.user.model.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookingNumber = UUID.randomUUID().toString();
    private LocalDateTime bookingDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public Booking(User user, Flight flight) {
        this.user = user;
        this.flight = flight;
    }
}
