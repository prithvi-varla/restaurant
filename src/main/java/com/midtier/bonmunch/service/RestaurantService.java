package com.midtier.bonmunch.service;

import com.midtier.bonmunch.repository.model.ReviewDTO;
import com.midtier.bonmunch.web.model.Review;
import com.midtier.bonmunch.repository.dao.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class RestaurantService {

    @Autowired
    ReviewRepository reviewRepository;

    public Mono<Review> createReview(Mono<Review> review) {

        return
                review.map(us -> getReviewDTOObject(us))
                     .publishOn(Schedulers.elastic())
                     .flatMap(reviewRepository::save)
                     .publishOn(Schedulers.elastic())
                     .subscribeOn(Schedulers.parallel())
                     .map(reviewDTO -> getReviewObject(reviewDTO));


    }

    public ReviewDTO getReviewDTOObject(Review review) {
        return ReviewDTO.builder()
                        .reviewId(UUID.randomUUID())
                        .companyId(review.getCompanyId())
                        .emailId(review.getEmailId())
                        .name(review.getName())
                        .phoneNumber(review.getPhoneNumber())
                        .review(review.getReview())
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();
    }

    public Review getReviewObject(ReviewDTO reviewDTO) {
        return Review.builder()
                     .reviewId(reviewDTO.getReviewId())
                     .companyId(reviewDTO.getCompanyId())
                     .emailId(reviewDTO.getEmailId())
                     .name(reviewDTO.getName())
                     .phoneNumber(reviewDTO.getPhoneNumber())
                     .review(reviewDTO.getReview())
                     .build();
    }
}
