package com.midtier.bonmunch.repository.model;

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
@Document(collection = "customers")
public class CustomerDTO {

    @Id
    private UUID customerId;

    private UUID adminId;

    private String customerName;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;
}
