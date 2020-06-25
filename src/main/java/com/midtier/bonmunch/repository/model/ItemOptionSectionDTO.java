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
public class ItemOptionSectionDTO {

    private UUID itemOptionSectionId;
    private UUID productId;
    private String name;
    private String description;
    private boolean required;
    private String minAllowed;
    private String maxAllowed;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<ItemOptionDTO> itemOption;
}
