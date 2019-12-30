package com.orderfresh.midtier.restaurant.repository.model;

import com.orderfresh.midtier.restaurant.web.model.OrderItemOption1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class OrderItemDTO {

    private UUID orderItemId;
    private UUID orderId;
    private UUID menuItemId;
    private String quantity;
    private String itemInstructions;
    private List<OrderItemOptionDTO> orderItemOptions;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
