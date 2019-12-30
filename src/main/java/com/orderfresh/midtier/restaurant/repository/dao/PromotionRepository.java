package com.orderfresh.midtier.restaurant.repository.dao;

import com.orderfresh.midtier.restaurant.model.Promotion;
import com.orderfresh.midtier.restaurant.model.Review;
import com.orderfresh.midtier.restaurant.repository.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
public interface PromotionRepository extends ReactiveMongoRepository<Promotion, Integer> {

    Mono<List<Promotion>> findByRestaurantId(String promotionId);


}
