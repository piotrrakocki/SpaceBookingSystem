package com.myproject.spacebooking.flight.service;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.booking.repository.BookingRepository;
import com.myproject.spacebooking.exceptions.FLightNotFoundException;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.flight.model.FlightStatus;
import com.myproject.spacebooking.flight.repository.FlightRepository;
import com.myproject.spacebooking.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private BookingRepository bookingRepository;
    private FlightServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new FlightServiceImpl(flightRepository, bookingRepository);
    }

    @Test
    void canGetAllFlights() {
        // when
        underTest.getAllFlights();

        // then
        verify(flightRepository).findAll();
    }

    @Test
    void canGetFlightsToBook() {
        // given
        Flight scheduledFlight = new Flight();
        scheduledFlight.setFlightStatus(FlightStatus.SCHEDULE);
        flightRepository.save(scheduledFlight);

        Flight inProgressFlight = new Flight();
        inProgressFlight.setFlightStatus(FlightStatus.IN_PROGRESS);
        flightRepository.save(inProgressFlight);

        Flight completedFlight = new Flight();
        completedFlight.setFlightStatus(FlightStatus.COMPLETED);
        flightRepository.save(completedFlight);

        List<Flight> flightToBook = new ArrayList<>();
        flightToBook.add(scheduledFlight);
        flightToBook.add(inProgressFlight);
        flightToBook.add(completedFlight);



        // when
        when(flightRepository.findAll()).thenReturn(flightToBook);
        List<Flight> result = underTest.getFlightsToBook();

        // then
        assertFalse(result.contains(inProgressFlight));
        assertFalse(result.contains(completedFlight));
        assertTrue(result.contains(scheduledFlight));
    }

    @Test
    void canGetFlightById() {
        // given
        long id = 10;
        Flight flight = new Flight();
        flight.setId(id);
        given(flightRepository.findById(id)).willReturn(Optional.of(flight));

        // when
        Optional<Flight> result = underTest.getFlightById(id);

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(flight);
    }

    @Test
    void canAddFlight() {
        // given
        Flight flight = new Flight();

        // when
        underTest.addFlight(flight);

        // then
        ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository).save(flightArgumentCaptor.capture());

        Flight capturedFlight = flightArgumentCaptor.getValue();
        assertThat(capturedFlight).isEqualTo(flight);
    }

    @Test
    void canUpdateFlight() {
        // given
        Flight flight = new Flight();

        // when
        underTest.updateFlight(flight);

        // then
        ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository).save(flightArgumentCaptor.capture());

        Flight capturedFlight = flightArgumentCaptor.getValue();
        assertThat(capturedFlight).isEqualTo(flight);
    }

    @Test
    void canDeleteFLight() {
        // given
        long id = 10;
        given(flightRepository.existsById(id)).willReturn(true);

        // when
        underTest.deleteFlight(id);

        // then
        verify(flightRepository).deleteById(id);
    }

    @Test
    void willThorwFLightNotFoundException() {
        // given
        long id = 10;
        given(flightRepository.existsById(id)).willReturn(false);

        // when
        assertThatThrownBy(() -> underTest.deleteFlight(id))
                .isInstanceOf(FLightNotFoundException.class)
                .hasMessageContaining("Flight with ID " + id + " does not exist.");

        // then
        verify(flightRepository, never()).deleteById(id);
    }

    @Test
    void canGetCompleatedFlightsByUser() {
        // given
        User user = new User();
        Flight completedFlight1 = new Flight();
        completedFlight1.setFlightStatus(FlightStatus.COMPLETED);
        Flight completedFlight2 = new Flight();
        completedFlight2.setFlightStatus(FlightStatus.COMPLETED);
        Flight flightInProgress = new Flight();
        flightInProgress.setFlightStatus(FlightStatus.IN_PROGRESS);

        Booking booking1 = new Booking(user, completedFlight1);
        Booking booking2 = new Booking(user, completedFlight2);
        Booking booking3 = new Booking(user, flightInProgress);

        List<Booking> userBookings = new ArrayList<>();
        userBookings.add(booking1);
        userBookings.add(booking2);
        userBookings.add(booking3);

        // when
        when(bookingRepository.findByUser(user)).thenReturn(userBookings);

        // then
        List<Flight> result = underTest.getCompleatedFlightsByUser(user);
        assertEquals(2, result.size());
        assertTrue(result.contains(completedFlight1));
        assertTrue(result.contains(completedFlight2));
        assertFalse(result.contains(flightInProgress));
    }

}