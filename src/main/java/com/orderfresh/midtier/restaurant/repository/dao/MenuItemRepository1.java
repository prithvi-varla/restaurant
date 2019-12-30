package com.orderfresh.midtier.restaurant.repository.dao;

import com.orderfresh.midtier.restaurant.repository.model.MenuItemDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface MenuItemRepository1 extends ReactiveMongoRepository<MenuItemDTO, UUID> {

    Mono<MenuItemDTO> findByMenuItemId(UUID menuItemId);


}