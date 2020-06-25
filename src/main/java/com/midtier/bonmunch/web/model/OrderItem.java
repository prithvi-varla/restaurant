package com.midtier.bonmunch.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private UUID orderItemId;
    private UUID orderId;
    private UUID productId;
    private String quantity;
    private String itemInstructions;
    private List<OrderItemOption> orderItemOptions;

}
