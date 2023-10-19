package com.myproject.spacebooking.flight.service;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.booking.repository.BookingRepository;
import com.myproject.spacebooking.exceptions.FLightNotFoundException;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.flight.model.FlightStatus;
import com.myproject.spacebooking.flight.repository.FlightRepository;
import com.myproject.spacebooking.user.model.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public List<Flight> getFlightsToBook() {
        List<Flight> filteredFlights = getAllFlights().stream()
                .filter(flight -> flight.getFlightStatus() != FlightStatus.IN_PROGRESS && flight.getFlightStatus() != FlightStatus.COMPLETED)
                .collect(Collectors.toList());

        return filteredFlights;
    }

    @Override
    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    @Override
    public void addFlight(@NonNull Flight flight) {
        flightRepository.save(flight);
    }

    @Override
    public void updateFlight(Flight flight) {
        flightRepository.save(flight);
    }

    @Override
    public void deleteFlight(Long id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
        } else {
            throw new FLightNotFoundException("Flight with ID " + id + " does not exist.");
        }
    }

    @Override
    public List<Flight> getCompleatedFlightsByUser(User user) {
        List<Flight> compleatedFlights = new ArrayList<>();
        List<Booking> userBookings = bookingRepository.findByUser(user);

        for (Booking booking : userBookings) {
            if (booking.getFlight().getFlightStatus() == FlightStatus.COMPLETED) {
                compleatedFlights.add(booking.getFlight());
            }
        }
        return compleatedFlights;
    }
}
