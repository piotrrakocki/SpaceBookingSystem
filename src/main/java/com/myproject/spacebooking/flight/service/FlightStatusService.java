package com.myproject.spacebooking.flight.service;

import com.myproject.spacebooking.flight.model.FlightStatus;
import com.myproject.spacebooking.flight.repository.FlightRepository;
import com.myproject.spacebooking.flight.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class FlightStatusService {

    private FlightRepository flightRepository;

    @Autowired
    public FlightStatusService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void updateFlightStatus() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Flight> flights = flightRepository.findAll();

        for (Flight flight : flights) {
            LocalDateTime deparrtueDateTime = flight.getDepartureDate();
            LocalDateTime arrivalDateTime = flight.getArrivalDate();

            if (deparrtueDateTime != null && arrivalDateTime != null) {
                if (currentDateTime.isBefore(deparrtueDateTime)) {
                    flight.setFlightStatus(FlightStatus.SCHEDULE);
                } else if (currentDateTime.isAfter(deparrtueDateTime) && currentDateTime.isBefore(arrivalDateTime)) {
                    flight.setFlightStatus(FlightStatus.IN_PROGRESS);
                } else if (currentDateTime.isAfter(arrivalDateTime)) {
                    flight.setFlightStatus(FlightStatus.COMPLETED);
                }
                flightRepository.save(flight);
            }
        }
    }
}
