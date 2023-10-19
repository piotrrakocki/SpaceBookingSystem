package com.myproject.spacebooking.booking.repository;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.flight.model.FlightStatus;
import com.myproject.spacebooking.user.model.User;
import com.myproject.spacebooking.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    private final UserRepository userRepository;
    private final BookingRepository underTest;

    @Autowired
    BookingRepositoryTest(UserRepository userRepository, BookingRepository underTest) {
        this.userRepository = userRepository;
        this.underTest = underTest;
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShoudFindByUser() {
        // given
        User user = new User();
        User savedUser = userRepository.save(user);

        Booking booking = new Booking();
        booking.setUser(savedUser);

        // when
        underTest.save(booking);
        List<Booking> result = underTest.findByUser(savedUser);

        // then
        System.out.println(result);
        assertEquals(1, result.size());
        assertEquals(booking, result.get(0));

    }

    @Test
    void itShoudFindCompletedBookings() {
        // given
        User user = new User();

        Flight flight = new Flight();
        flight.setFlightStatus(FlightStatus.COMPLETED);

        Booking booking1 = new Booking(user, flight);
        Booking booking2 = new Booking(user, flight);
        Booking booking3 = new Booking();
        Booking booking4 = new Booking();
        booking4.setFlight(flight);

        underTest.save(booking1);
        underTest.save(booking2);
        underTest.save(booking3);
        underTest.save(booking4);

        // when
        List<Booking> completedBookings = underTest.findCompletedBookings(user.getUserId());

        // then
        assertEquals(2, completedBookings.size());
    }

    @Test
    void itShoudFindInProgressBookings() {
        // given
        User user = new User();

        Flight flight = new Flight();
        flight.setFlightStatus(FlightStatus.IN_PROGRESS);

        Booking booking1 = new Booking(user, flight);
        Booking booking2 = new Booking(user, flight);
        Booking booking3 = new Booking();
        Booking booking4 = new Booking();
        booking4.setFlight(flight);

        underTest.save(booking1);
        underTest.save(booking2);
        underTest.save(booking3);
        underTest.save(booking4);

        // when
        List<Booking> completedBookings = underTest.findInProgressBookings(user.getUserId());

        // then
        assertEquals(2, completedBookings.size());
    }

    @Test
    void itShoudFindScheduleBookings() {
        // given
        User user = new User();

        Flight flight = new Flight();
        flight.setFlightStatus(FlightStatus.SCHEDULE);

        Booking booking1 = new Booking(user, flight);
        Booking booking2 = new Booking(user, flight);
        Booking booking3 = new Booking();
        Booking booking4 = new Booking();
        booking4.setFlight(flight);

        underTest.save(booking1);
        underTest.save(booking2);
        underTest.save(booking3);
        underTest.save(booking4);

        // when
        List<Booking> completedBookings = underTest.findScheduleBookings(user.getUserId());

        // then
        assertEquals(2, completedBookings.size());
    }
}