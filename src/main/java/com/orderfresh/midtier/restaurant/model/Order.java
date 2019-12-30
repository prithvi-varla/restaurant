package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private int orderId;

    private int user_id;

    private int restaurant_id;

    private float subtotal;

    private float tax;

    private float tip;

    private float delivery_fee;

    private float total;

    private String delivery_instructions;

    private List<OrderItem> order_items_attributes;

}
