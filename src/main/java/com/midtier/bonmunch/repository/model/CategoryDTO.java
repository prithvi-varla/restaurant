package com.midtier.bonmunch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@Document(collection = "categories")
public class CategoryDTO {

    @Id
    private UUID categoryId;
    private UUID companyId;
    private String categoryName;
    private String sortOrder;
    private String categoryDescription;
    private UUID parentCategoryId;
    private Boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
