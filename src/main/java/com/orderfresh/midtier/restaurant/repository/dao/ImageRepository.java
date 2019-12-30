package com.orderfresh.midtier.restaurant.repository.dao;

import com.orderfresh.midtier.restaurant.model.Image;
import com.orderfresh.midtier.restaurant.repository.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface  ImageRepository extends ReactiveMongoRepository<Image, String> {

    Mono<Image> findByName(String name);




}
