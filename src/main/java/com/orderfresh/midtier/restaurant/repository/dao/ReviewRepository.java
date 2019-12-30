package com.orderfresh.midtier.restaurant.repository.dao;

import com.orderfresh.midtier.restaurant.model.Review;
import com.orderfresh.midtier.restaurant.repository.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, Integer> {

    Mono<CustomerDTO> findByReviewId(int reviewId);




}