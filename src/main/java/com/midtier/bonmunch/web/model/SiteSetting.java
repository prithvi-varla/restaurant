package com.midtier.bonmunch.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteSetting {

    private boolean floatingLandingPage;
    private boolean activeReviewPage;
    private boolean activeOrders;
    private boolean activeGallery;

}
