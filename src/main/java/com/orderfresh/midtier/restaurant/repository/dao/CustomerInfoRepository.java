package com.orderfresh.midtier.restaurant.repository.dao;

import com.orderfresh.midtier.restaurant.repository.model.CustomerInfoDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerInfoRepository extends ReactiveMongoRepository<CustomerInfoDTO, UUID> {

    Mono<CustomerInfoDTO> findByCustomerId(UUID adminId);




}

