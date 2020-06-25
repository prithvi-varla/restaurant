package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<CustomerDTO, UUID> {

    Mono<CustomerDTO> findByCustomerId(UUID customerId);

}
