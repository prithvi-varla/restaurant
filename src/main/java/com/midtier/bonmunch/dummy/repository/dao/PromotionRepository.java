package com.midtier.bonmunch.dummy.repository.dao;

import com.midtier.bonmunch.dummy.model.Promotion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
public interface PromotionRepository extends ReactiveMongoRepository<Promotion, Integer> {

    Mono<List<Promotion>> findByRestaurantId(String promotionId);

}
