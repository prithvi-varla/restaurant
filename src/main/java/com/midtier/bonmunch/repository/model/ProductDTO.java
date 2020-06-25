package com.midtier.bonmunch.repository.model;

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
@Document(collection = "Products")
public class ProductDTO {

    @Id
    private UUID productId;
    private UUID userId;
    private UUID companyId;
    private UUID categoryId;
    private String productCategoryMapName;
    private String name ;
    private String price;
    private String description;
    private List<ItemOptionSectionDTO> itemOptionSection;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
