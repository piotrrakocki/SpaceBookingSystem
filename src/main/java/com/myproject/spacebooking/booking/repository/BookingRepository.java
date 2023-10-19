package com.myproject.spacebooking.booking.repository;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user = :user")
    List<Booking> findByUser(@Param("user") User user);

    @Query("SELECT b FROM Booking b " +
            "INNER JOIN b.flight f " +
            "WHERE b.user.id = :userId " +
            "AND f.flightStatus = 'COMPLETED'")
    List<Booking> findCompletedBookings(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b " +
            "INNER JOIN b.flight f " +
            "WHERE b.user.id = :userId " +
            "AND f.flightStatus = 'IN_PROGRESS'")
    List<Booking> findInProgressBookings(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b " +
            "INNER JOIN b.flight f " +
            "WHERE b.user.id = :userId " +
            "AND f.flightStatus = 'SCHEDULE'")
    List<Booking> findScheduleBookings(@Param("userId") Long userId);
}
