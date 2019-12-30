package com.orderfresh.midtier.restaurant.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class OrderItemOptionDTO {

    private UUID orderItemOptionId;
    private UUID orderItemId;
    private UUID itemOptionId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
