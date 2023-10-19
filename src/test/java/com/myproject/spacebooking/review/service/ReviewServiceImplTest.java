package com.myproject.spacebooking.review.service;

import com.myproject.spacebooking.exceptions.ReviewNotFoundException;
import com.myproject.spacebooking.review.model.Review;
import com.myproject.spacebooking.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    private ReviewServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    void canGetAllReviews() {
        // when
        underTest.getAllReviews();

        // then
        verify(reviewRepository).findAll();
    }

    @Test
    void canAddReview() {
        // given
        Review review = new Review();

        // when
        underTest.addReview(review);

        // then
        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewArgumentCaptor.capture());

        Review capturedReview = reviewArgumentCaptor.getValue();
        assertThat(capturedReview).isEqualTo(review);
    }

    @Test
    void canUpdateReciew() {
        // given
        Review review = new Review();

        // when
        underTest.updateReview(review);

        // then
        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewArgumentCaptor.capture());

        Review capturedReview = reviewArgumentCaptor.getValue();
        assertThat(capturedReview).isEqualTo(review);
    }

    @Test
    void canGetReviewById() {
        // given
        long id = 10;
        Review review = new Review();
        review.setId(id);
        given(reviewRepository.findById(id)).willReturn(Optional.of(review));

        // when
        Optional<Review> result = underTest.getReviewById(id);

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(review);
    }

    @Test
    void canDeleteReview() {
        // given
        long id = 10;
        given(reviewRepository.existsById(id)).willReturn(true);

        // when
        underTest.deleteReview(id);

        // then
        verify(reviewRepository).deleteById(id);
    }

    @Test
    void willThrowReviewNotFoundException() {
        // given
        long id = 10;
        given(reviewRepository.existsById(id)).willReturn(false);

        // when
        assertThatThrownBy(() -> underTest.deleteReview(id))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining("Review with id " + id + " does not exist.");

        // then
        verify(reviewRepository, never()).deleteById(id);
    }
}