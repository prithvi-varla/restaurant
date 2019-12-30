package com.orderfresh.midtier.restaurant.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderfresh.midtier.restaurant.web.model.ItemOption1;
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
    private UUID menuItemId;
    private String name;
    private String description;
    private boolean required;
    private String minAllowed;
    private String maxAllowed;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<ItemOptionDTO> itemOption;
}
