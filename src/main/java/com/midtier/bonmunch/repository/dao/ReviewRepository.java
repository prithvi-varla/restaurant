package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.ReviewDTO;
import com.midtier.bonmunch.web.model.Review;
import com.midtier.bonmunch.repository.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<ReviewDTO, UUID> {

    Mono<Review> findByReviewId(UUID reviewId);

}