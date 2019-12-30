package com.orderfresh.midtier.restaurant.repository.dao;

import com.orderfresh.midtier.restaurant.model.Person;
import com.orderfresh.midtier.restaurant.repository.model.PersonDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

/**
 * Repository interface to manage {@link Person} instances.
 *
 * @author Mark Paluch
 */
@Repository
public interface ReactivePersonRepository extends ReactiveMongoRepository<PersonDTO, UUID> {


    @Query("{ 'firstname': ?0, 'lastname': ?1}")
    Mono<PersonDTO> findByFirstnameAndLastname(String firstname, String lastname);

}

