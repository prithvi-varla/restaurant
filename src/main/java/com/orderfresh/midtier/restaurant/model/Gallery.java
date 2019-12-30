package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {

    private int orderId;
    private String thumbnailSrc;
    private String fullSizeSrc;
    private String alt;
    private String title;
}
