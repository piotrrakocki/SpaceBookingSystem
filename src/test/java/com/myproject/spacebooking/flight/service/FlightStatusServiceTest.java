package com.myproject.spacebooking.flight.service;

import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.flight.model.FlightStatus;
import com.myproject.spacebooking.flight.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightStatusServiceTest {

    @Mock
    private FlightRepository flightRepository;
    private FlightStatusService underTest;

    @BeforeEach
    void setUp() {
        underTest = new FlightStatusService(flightRepository);
    }

    @Test
    void canUpdateFlightStatus() {
        // given
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime departueDateTime = currentDateTime.plusMinutes(30);
        LocalDateTime arrivalDateTime = currentDateTime.plusMinutes(90);

        Flight scheduledFlight = new Flight();
        scheduledFlight.setFlightStatus(FlightStatus.SCHEDULE);
        scheduledFlight.setDepartureDate(departueDateTime);
        scheduledFlight.setArrivalDate(arrivalDateTime);

        Flight inProgressFlight = new Flight();
        inProgressFlight.setFlightStatus(FlightStatus.IN_PROGRESS);
        inProgressFlight.setDepartureDate(currentDateTime.minusMinutes(30));
        inProgressFlight.setArrivalDate(currentDateTime.plusMinutes(30));

        Flight completedFlight = new Flight();
        completedFlight.setFlightStatus(FlightStatus.COMPLETED);
        completedFlight.setDepartureDate(currentDateTime.minusMinutes(90));
        completedFlight.setArrivalDate(currentDateTime.minusMinutes(30));

        List<Flight> flights = Arrays.asList(scheduledFlight, inProgressFlight, completedFlight);

        // when
        when(flightRepository.findAll()).thenReturn(flights);
        underTest.updateFlightStatus();

        // then
        verify(flightRepository, times(3)).save(any(Flight.class));
        verify(flightRepository).save(scheduledFlight);
        verify(flightRepository).save(inProgressFlight);
        verify(flightRepository).save(completedFlight);
    }
}