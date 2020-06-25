package com.midtier.bonmunch.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private UUID categoryId;
    private UUID companyId;
    private String categoryName;
    private String sortOrder;
    private String categoryDescription;
    private UUID parentCategoryId;
    private String categoryMapName;
    private Boolean active;

}