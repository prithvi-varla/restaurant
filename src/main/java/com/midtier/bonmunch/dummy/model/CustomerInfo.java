package com.midtier.bonmunch.dummy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CustomerInfo {

    private UUID customerId;

    private String title;

    private String description;

    private String contactUs;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;
}
