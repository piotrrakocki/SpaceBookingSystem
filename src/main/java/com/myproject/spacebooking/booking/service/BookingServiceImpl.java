package com.myproject.spacebooking.booking.service;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.booking.repository.BookingRepository;
import com.myproject.spacebooking.exceptions.BookingNotFoundException;
import com.myproject.spacebooking.exceptions.NoAvailableSeatsException;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.flight.model.FlightStatus;
import com.myproject.spacebooking.flight.service.FlightService;
import com.myproject.spacebooking.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, FlightService flightService) {
        this.bookingRepository = bookingRepository;
        this.flightService = flightService;
    }

    @Transactional
    @Override
    public Booking bookFlight(User user, Flight flight) {
        if (flight.getAvailableSeats() <= 0) {
            throw new NoAvailableSeatsException("No Avitable Seats");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightService.updateFlight(flight);

        Booking booking = new Booking(user, flight);
        booking.setBookingDate(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            Flight flight = booking.getFlight();

            if (flight.getFlightStatus() != FlightStatus.COMPLETED && flight.getFlightStatus() != FlightStatus.IN_PROGRESS) {
                flight.setAvailableSeats(flight.getAvailableSeats() + 1);
                flightService.updateFlight(flight);

                bookingRepository.deleteById(bookingId);
            }
        } else {
            throw new BookingNotFoundException("Booking with ID " + bookingId + " does not exist.");
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getCompletedBookings(Long userId) {
        return bookingRepository.findCompletedBookings(userId);
    }

    @Override
    public List<Booking> getInProgressBookings(Long userId) {
        return bookingRepository.findInProgressBookings(userId);
    }

    @Override
    public List<Booking> getScheduleBookings(Long userId) {
        return bookingRepository.findScheduleBookings(userId);
    }
}
