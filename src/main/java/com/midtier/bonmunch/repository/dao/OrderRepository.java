package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.OrderDTO;
import com.midtier.bonmunch.repository.model.CustomerDTO;
import com.midtier.bonmunch.repository.model.OrderDTO;
import com.midtier.bonmunch.repository.model.UserDTO;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<OrderDTO, UUID> {

    Mono<OrderDTO> findByOrderId(UUID orderId);

    Flux<OrderDTO> findByCompanyId(UUID companyId);

    Flux<OrderDTO> findTop5ByCompanyIdOrderByCreatedDateDesc(UUID companyId);

    Flux<OrderDTO> findByCompanyIdAndCreatedDateGreaterThan(UUID companyId, LocalDateTime
            requestedDate);

    Flux<OrderDTO> findByCompanyIdAndCreatedDateLessThan(UUID companyId, LocalDateTime
            requestedDate);

    //Flux<OrderDTO> findByCompanyIdAndCreatedDateLessThanAndGreaterThan(UUID companyId, LocalDateTime
     //       endDate, LocalDateTime startDate);

    Flux<OrderDTO> findByCompanyIdAndCreatedDateBetween(UUID companyId, LocalDateTime startDate, LocalDateTime endDate);

//    @Query("{ 'firstName': ?0, 'password': ?1}")
//    Mono<UserDTO> findByFirstNameAndPassword(String firstName, String password);





}