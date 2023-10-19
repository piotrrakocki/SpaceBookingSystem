package com.myproject.spacebooking.booking.service;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.user.model.User;

import java.util.List;

public interface BookingService {

    Booking bookFlight(User user, Flight flight);

    void cancelBooking(Long bookingId);

    List<Booking> getAllBookings();

    List<Booking> getCompletedBookings(Long userId);

    List<Booking> getInProgressBookings(Long userId);

    List<Booking> getScheduleBookings(Long userId);
}
