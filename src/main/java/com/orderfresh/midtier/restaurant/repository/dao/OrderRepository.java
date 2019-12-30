package com.orderfresh.midtier.restaurant.repository.dao;

import com.orderfresh.midtier.restaurant.model.Order;
import com.orderfresh.midtier.restaurant.repository.model.CustomerDTO;
import com.orderfresh.midtier.restaurant.repository.model.OrderDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, Integer> {

    Mono<CustomerDTO> findByOrderId(int orderId);




}
