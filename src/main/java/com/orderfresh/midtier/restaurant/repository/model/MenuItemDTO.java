package com.orderfresh.midtier.restaurant.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderfresh.midtier.restaurant.web.model.ItemOptionSection1;
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
@Document(collection = "MenuItems")
public class MenuItemDTO {

    @Id
    private UUID menuItemId;
    private UUID userId;
    private UUID companyId;
    private String name ;
    private String price;
    private String description;
    private List<ItemOptionSectionDTO> itemOptionSection;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
