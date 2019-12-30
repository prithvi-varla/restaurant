package com.orderfresh.midtier.restaurant.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Customer {

    private UUID adminId;

    private UUID customerId;

    private String customerName;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;
}
