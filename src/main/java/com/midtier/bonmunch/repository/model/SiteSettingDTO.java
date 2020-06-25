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
@Document(collection = "siteSetting")
public class SiteSettingDTO {

    @Id
    private UUID siteSettingId;
    private UUID companyId;
    private boolean floatingLandingPage;
    private boolean activeReviewPage;
    private boolean activeOrders;
    private boolean activeGallery;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
