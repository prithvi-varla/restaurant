package com.midtier.bonmunch.dummy.repository.dao;

import com.midtier.bonmunch.dummy.model.Person;
import com.midtier.bonmunch.dummy.model.Person2;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface to manage {@link Person} instances.
 *
 * @author prithvi
 */
@Repository
public interface ReactivePersonRepository2 extends ReactiveMongoRepository<Person2, UUID> {

    /**
     * Derived query selecting by {@code lastname}.
     *
     * @param lastname
     * @return
     */
    Flux<Person2> findByLastname(String lastname);

    /**
     * String query selecting one entity.
     *
     * @param lastname
     * @return
     */
    @Query("{ 'firstname': ?0, 'lastname': ?1}")
    Mono<Person2> findByFirstnameAndLastname(String firstname, String lastname);

    /**
     * Derived query selecting by {@code lastname}. {@code lastname} uses deferred resolution that does not require
     * blocking to obtain the parameter value.
     *
     * @param lastname
     * @return
     */
    Flux<Person2> findByLastname(Mono<String> lastname);

    /**
     * Derived query selecting by {@code firstname} and {@code lastname}. {@code firstname} uses deferred resolution that
     * does not require blocking to obtain the parameter value.
     *
     * @param firstname
     * @param lastname
     * @return
     */
    Mono<Person2> findByFirstnameAndLastname(Mono<String> firstname, String lastname);

    /**
     * Use a tailable cursor to emit a stream of entities as new entities are written to the capped collection.
     *
     * @return
     */
    @Tailable
    Flux<Person2> findWithTailableCursorBy();
}

