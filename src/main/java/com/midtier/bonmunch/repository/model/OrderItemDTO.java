package com.midtier.bonmunch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class OrderItemDTO {

    private UUID orderItemId;
    private UUID orderId;
    private UUID productId;
    private String quantity;
    private String itemInstructions;
    private List<OrderItemOptionDTO> orderItemOptions;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
