package com.myproject.spacebooking.review.service;

import com.myproject.spacebooking.review.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    List<Review> getAllReviews();

    Review addReview(Review review);

    Review updateReview(Review review);

    Optional<Review> getReviewById(Long reviewId);

    void deleteReview(Long id);
}
