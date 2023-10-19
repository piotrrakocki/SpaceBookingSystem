package com.myproject.spacebooking.booking.controller;

import com.myproject.spacebooking.exceptions.NoAvailableSeatsException;
import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.user.model.User;
import com.myproject.spacebooking.booking.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class BookingController {

    public final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/bookFlight")
    public String bookFlight(Authentication authentication,
                             @RequestParam Flight flight,
                             RedirectAttributes redirectAttributes) {
        User user = (User) authentication.getPrincipal();

        try {
            bookingService.bookFlight(user, flight);
            return "redirect:/flights";
        } catch (NoAvailableSeatsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No Available Seats");
            return "redirect:/flights";
        }
    }
}
