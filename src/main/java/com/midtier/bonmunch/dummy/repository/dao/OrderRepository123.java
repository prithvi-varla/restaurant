package com.midtier.bonmunch.dummy.repository.dao;

import com.midtier.bonmunch.dummy.model.Order;
import com.midtier.bonmunch.repository.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository123 extends ReactiveMongoRepository<Order, Integer> {

    Mono<CustomerDTO> findByOrderId(int orderId);




}
