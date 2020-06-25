package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.UserDTO;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDTO, UUID> {

    Mono<UserDTO> findByUserId(UUID userId);

    Mono<UserDTO> findByEmailAddress(String emailAddress);

    Flux<UserDTO> findByCompanyId(UUID companyId);

    Flux<UserDTO> findTop5ByCompanyIdOrderByCreatedDateDesc(UUID companyId);

    Flux<UserDTO> findByCompanyIdAndCreatedDateGreaterThan(UUID companyId, LocalDateTime
            requestedDate);

    Flux<UserDTO> findByCompanyIdAndCreatedDateLessThan(UUID companyId, LocalDateTime
            requestedDate);

   /* Mono<UserDTO> findByEmailAddressAndPassword(String email, String password);*/

    /*@Query("{ 'emailAddress': ?0, 'password': ?1}")
    Mono<UserDTO> findByEmailAddressAndPassword(String email, String password);


    @Query("{ 'firstName': ?0, 'password': ?1}")
    Mono<UserDTO> findByFirstNameAndPassword(String firstName, String password);


    @Query("{ 'password': ?0}")
    Mono<UserDTO> findByPassword(String password);

    @Query("{ 'firstName': ?0, 'lastName': ?1}")
    Mono<UserDTO> findByFirstnameAndLastname(String firstname, String lastname);*/




}
