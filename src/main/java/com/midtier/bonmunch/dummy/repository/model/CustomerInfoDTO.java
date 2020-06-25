package com.midtier.bonmunch.dummy.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers_info")
public class CustomerInfoDTO {

    @Id
    private UUID customerInfoId;

    private UUID customerId;

    private String title;

    private String description;

    private String contactUs;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;
}
