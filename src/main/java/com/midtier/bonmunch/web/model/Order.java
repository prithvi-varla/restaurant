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
public class Order {

    private UUID orderId;
    private UUID userId;
    private UUID companyId;

    private String orderNumber;
    private String subTotal;
    private String tax;
    private String tip;
    private String deliveryFee;
    private String total;
    private OrderStatus orderStatus;
    private String deliveryInstructions;
    private List<OrderItem> orderItems;
}

