package com.myproject.spacebooking.booking.service;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.booking.repository.BookingRepository;
import com.myproject.spacebooking.exceptions.BookingNotFoundException;
import com.myproject.spacebooking.exceptions.NoAvailableSeatsException;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.flight.model.FlightStatus;
import com.myproject.spacebooking.flight.service.FlightServiceImpl;
import com.myproject.spacebooking.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private FlightServiceImpl flightService;
    private BookingServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new BookingServiceImpl(bookingRepository, flightService);
    }

    @Test
    void willThrowNoAvailableSeatsException() {
        // given
        User user = new User();
        Flight flight = new Flight();
        flight.setAvailableSeats(0);

        // when
        assertThatThrownBy(() -> underTest.bookFlight(user, flight))
                .isInstanceOf(NoAvailableSeatsException.class)
                .hasMessageContaining("No Avitable Seats");

        // then
        verify(flightService, never()).updateFlight(flight);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void canBookFlight() {
        // given
        User user = new User();
        Flight flight = new Flight();
        flight.setAvailableSeats(5);

        // when
        underTest.bookFlight(user, flight);

        // then
        verify(flightService).updateFlight(flight);
        verify(bookingRepository).save(any(Booking.class));
        assertEquals(4, flight.getAvailableSeats());
    }

    @Test
    void willThrowBookingNotFoundException() {
        // given
        Flight flight = new Flight();
        long bookingId = 0;

        // when
        assertThatThrownBy(() -> underTest.cancelBooking(bookingId))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining("Booking with ID " + bookingId + " does not exist.");

        // then
        verify(bookingRepository, never()).deleteById(bookingId);
        verify(flightService, never()).updateFlight(flight);
    }

    @Test
    void canCancelBooking() {
        // given
        User user = new User();
        Flight flight = new Flight();
        flight.setFlightStatus(FlightStatus.SCHEDULE);
        Booking booking = new Booking(user, flight);
        booking.setId(1L);

        // when
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        underTest.cancelBooking(booking.getId());

        // then
        verify(flightService).updateFlight(flight);
        verify(bookingRepository).deleteById(booking.getId());
    }

    @Test
    void bookingIsInProgress() {
        // given
        User user = new User();
        Flight flight = new Flight();
        flight.setFlightStatus(FlightStatus.IN_PROGRESS);
        flight.setAvailableSeats(5);
        Booking booking = new Booking(user, flight);
        booking.setId(1L);

        // when
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        underTest.cancelBooking(booking.getId());

        // then
        verify(flightService, never()).updateFlight(flight);
        verify(bookingRepository, never()).deleteById(booking.getId());
        assertEquals(5, flight.getAvailableSeats());
    }

    @Test
    void bookingIsCompleted() {
        // given
        User user = new User();
        Flight flight = new Flight();
        flight.setFlightStatus(FlightStatus.COMPLETED);
        flight.setAvailableSeats(5);
        Booking booking = new Booking(user, flight);
        booking.setId(1L);

        // when
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        underTest.cancelBooking(booking.getId());

        // then
        verify(flightService, never()).updateFlight(flight);
        verify(bookingRepository, never()).deleteById(booking.getId());
        assertEquals(5, flight.getAvailableSeats());
    }

    @Test
    void canGetAllBookings() {
        // when
        underTest.getAllBookings();

        // then
        verify(bookingRepository).findAll();
    }

    @Test
    void canGetCompletedBookings() {
        // given
        User user = new User();
        user.setUserId(1L);

        // when
        underTest.getCompletedBookings(user.getUserId());

        // then
        verify(bookingRepository).findCompletedBookings(user.getUserId());
    }

    @Test
    void canGetInProgressBookings() {
        // given
        User user = new User();
        user.setUserId(1L);

        // when
        underTest.getInProgressBookings(user.getUserId());

        // then
        verify(bookingRepository).findInProgressBookings(user.getUserId());
    }

    @Test
    void canGetScheduleBookings() {
        // given
        User user = new User();
        user.setUserId(1L);

        // whem
        underTest.getScheduleBookings(user.getUserId());

        // then
        verify(bookingRepository).findScheduleBookings(user.getUserId());
    }

}