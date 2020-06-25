package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.ProductDTO;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<ProductDTO, UUID> {

    @Query("{ 'productId': ?0, 'companyId': ?1}")
    Mono<ProductDTO> findByProductIdAndcompanyId(UUID productId, UUID companyId);

    Mono<ProductDTO> findByProductId(UUID productId);

    Flux<ProductDTO> findByCompanyId(UUID id);


}