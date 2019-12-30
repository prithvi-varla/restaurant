package com.orderfresh.midtier.restaurant.web.model;

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
public class ItemOption1  {

    private UUID itemOptionId;
    private UUID itemOptionSectionId;
    private String name;
    private String description;
    private String price;
}

