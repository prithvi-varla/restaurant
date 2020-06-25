package com.midtier.bonmunch.dummy.repository.dao;

import com.midtier.bonmunch.dummy.model.Person;
import com.midtier.bonmunch.repository.model.PersonDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

/**
 * Repository interface to manage {@link Person} instances.
 *
 * @author prithvi
 */
@Repository
public interface ReactivePersonRepository extends ReactiveMongoRepository<PersonDTO, UUID> {


    @Query("{ 'firstname': ?0, 'lastname': ?1}")
    Mono<PersonDTO> findByFirstnameAndLastname(String firstname, String lastname);

}

