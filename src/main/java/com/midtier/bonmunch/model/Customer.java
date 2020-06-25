package com.midtier.bonmunch.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private UUID adminId;

    private UUID customerId;

    private String customerName;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;
}
