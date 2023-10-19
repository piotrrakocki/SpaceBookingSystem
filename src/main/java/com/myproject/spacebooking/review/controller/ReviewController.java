package com.myproject.spacebooking.review.controller;

import com.myproject.spacebooking.flight.model.Flight;
import com.myproject.spacebooking.flight.service.FlightService;
import com.myproject.spacebooking.review.model.Review;
import com.myproject.spacebooking.review.service.ReviewService;
import com.myproject.spacebooking.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final FlightService flightService;

    @Autowired
    public ReviewController(ReviewService reviewService, FlightService flightService) {
        this.reviewService = reviewService;
        this.flightService = flightService;
    }

    @GetMapping("/review")
    public String getReviews(Model model, Authentication authentication) {

        User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        }


        List<Review> review = reviewService.getAllReviews();
        List<Flight> completedFlights = flightService.getCompleatedFlightsByUser(user);
        model.addAttribute("reviewsList", review);
        model.addAttribute("review", new Review());
        model.addAttribute("completedFlights", completedFlights);

        List<Integer> avitableRatings = Arrays.asList(1, 2, 3, 4, 5);
        model.addAttribute("avitableRatings", avitableRatings);
        return "review";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/addReview")
    public String addEditReview(@RequestParam(required = false, defaultValue = "-1") Long id, Model model) {
        log.info("editing id = {}", id);
        Optional<Review> review = reviewService.getReviewById(id);
        if (review == null) {
            Objects.equals(review, new Review());
        }
        model.addAttribute("review", review);
        model.addAttribute("error", model.getAttribute("error"));
        return "add_reviews";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/addReview")
    public String processReview(@ModelAttribute("review") Review review, Model model, Authentication authentication) {
        log.info("review from form = {}", review);

        User user = (User) authentication.getPrincipal();

        try {
            review.setUser(user);
            if (review.getId() == null) {
                reviewService.addReview(review);
            } else {
                reviewService.updateReview(review);
            }
            return "redirect:/review";
        } catch (IllegalArgumentException e) {
            model.addAttribute("review", review);
            model.addAttribute("error", "taken");
            return "review";
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') or #review.user.userId == principal.userId")
    @GetMapping("deleteReview")
    public String deleteReview(@RequestParam Long id) {
        log.info("Deleting review with id = {}", + id);
        reviewService.deleteReview(id);
        return "redirect:/review";

    }
}
