package com.midtier.bonmunch.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Product {

    private UUID productId;
    private UUID userId;
    private UUID companyId;
    private UUID categoryId;
    private String productCategoryMapName;
    private String name ;
    private String price;
    private String description;
    @JsonProperty("itemOptionSections")
    private List<ItemOptionSection> itemOptionSection;

}
