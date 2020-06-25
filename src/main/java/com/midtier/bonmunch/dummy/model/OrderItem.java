package com.midtier.bonmunch.dummy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Document(collection = "order_items")
public class OrderItem {

    private int order_id;

    private int menu_item_id;

    private float quantity;

    private String item_instructions;

    private List<OrderItemOption> order_item_options_attributes;
}
