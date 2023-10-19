package com.myproject.spacebooking.profile.controller;

import com.myproject.spacebooking.booking.model.Booking;
import com.myproject.spacebooking.booking.service.BookingService;
import com.myproject.spacebooking.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class ProfileController {

    private final BookingService bookingService;

    @Autowired
    public ProfileController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }

    @GetMapping("/profile/profile-account")
    public String showProfileAccount(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("appUser", user);
        if (user != null) {
            log.info("Phone number: ", user.getPhoneNumber());
            log.info("Email: ", user.getEmail());
        }
        return "profile-account";
    }

    @GetMapping("/profile/profile-flight")
    public String showProfileFlight(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getUserId();

        if (authentication != null && authentication.isAuthenticated()) {
            List<Booking> completedBookings = bookingService.getCompletedBookings(userId);
            List<Booking> inProgressBookings = bookingService.getInProgressBookings(userId);
            List<Booking> scheduledBookings = bookingService.getScheduleBookings(userId);
            model.addAttribute("completedBookings", completedBookings);
            model.addAttribute("inProgressBookings", inProgressBookings);
            model.addAttribute("scheduledBookings", scheduledBookings);
        }
        return "profile-flight";
    }

    @DeleteMapping("/cancelBooking")
    public String cancleBooking(@RequestParam Long bookingId) {
        log.info("Canceling booking with id={}", bookingId);
        bookingService.cancelBooking(bookingId);
        return "redirect:/profile/profile-flight";
    }
}
