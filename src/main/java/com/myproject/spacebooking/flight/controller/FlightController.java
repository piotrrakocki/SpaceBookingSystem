package com.myproject.spacebooking.flight.controller;

import com.myproject.spacebooking.flight.service.FlightService;
import com.myproject.spacebooking.rocket.service.RocketService;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.rocket.model.Rocket;
import com.myproject.spacebooking.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class FlightController {

    private final FlightService flightService;
    private final RocketService rocketService;

    @Autowired
    public FlightController(FlightService flightService, RocketService rocketService) {
        this.flightService = flightService;
        this.rocketService = rocketService;
    }

    @GetMapping("/flights")
    public String getAllFlights(@RequestParam(required = false) String error, Model model) {
        List<Flight> flights = flightService.getFlightsToBook();
        log.info("flights: ", flights);
        model.addAttribute("flights", flights);
        model.addAttribute("user", new User());
        model.addAttribute("error", error);
        return "flights";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/deleteFlight")
    public String deleteFlight(@RequestParam Long id) {
        log.info("Deleting flight with id = {}", id);
        flightService.deleteFlight(id);
        return "redirect:/flights";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/addFlights")
    public String addEditFlight(@RequestParam(required = false, defaultValue = "-1") Long id, Model model) {
        log.info("editing id = {}", id);
        Optional<Flight> flight = flightService.getFlightById(id);
        if (flight == null) {
            flight = Optional.of(new Flight());
        }
        List<Rocket> rockets = rocketService.getAllRockets();

        model.addAttribute("flight", flight);
        model.addAttribute("rockets", rockets);
        model.addAttribute("error", model.getAttribute("error"));
        return "add_flights";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/addFlights")
    public String processFlight(@ModelAttribute("flight") Flight flight, Model model) {
        log.info("flight from form = {}", flight);

        try {
            if (flight.getId() == null) {
                flightService.addFlight(flight);
            } else {
                flightService.updateFlight(flight);
            }

            return "redirect:/flights";
        } catch (IllegalArgumentException e) {
            model.addAttribute("flight", flight);
            model.addAttribute("error","taken");
            return "add_flight";
        }
    }
}
