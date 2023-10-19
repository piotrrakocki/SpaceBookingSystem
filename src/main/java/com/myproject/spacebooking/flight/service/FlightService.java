package com.myproject.spacebooking.flight.service;

import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.user.model.User;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    List<Flight> getAllFlights();

    List<Flight> getFlightsToBook();

    List<Flight> getCompleatedFlightsByUser(User user);

    Optional<Flight> getFlightById(Long id);

    void addFlight(Flight flight);

    void updateFlight(Flight flight);

    void deleteFlight(Long id);
}
