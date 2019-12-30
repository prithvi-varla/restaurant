package com.orderfresh.midtier.restaurant.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class ItemOptionDTO {

    private UUID itemOptionId;
    private UUID itemOptionSectionId;
    private String name;
    private String description;
    private String price;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
