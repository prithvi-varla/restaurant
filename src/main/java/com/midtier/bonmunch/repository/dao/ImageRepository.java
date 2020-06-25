package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.model.Image;
import com.midtier.bonmunch.web.model.ImageType;
import com.midtier.bonmunch.model.Image;
import com.midtier.bonmunch.repository.model.CustomerDTO;
import com.midtier.bonmunch.web.model.ImageType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface  ImageRepository extends ReactiveMongoRepository<Image, UUID> {

    Mono<Image> findByName(String name);

    Flux<Image> findByCompanyId(UUID id);

    Flux<Image> findByCompanyId(UUID id, Pageable pageable);

    @Query("{ 'imageType': ?0, 'companyId': ?1}")
    Flux<Image> findByImageTypeAndCompanyId(ImageType imageType, UUID companyId, Pageable pageable);


}
