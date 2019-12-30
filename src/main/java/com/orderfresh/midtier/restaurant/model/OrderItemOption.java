package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Document(collection = "order_item_option")
public class OrderItemOption {

    private int order_item_id;

    private int item_option_id;
}
