package com.orderfresh.midtier.restaurant.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@Document(collection = "companies")
public class CompanyDTO {

    @Id
    private UUID companyId;
    private String companyCode;
    private String companyName;
    private String address1;
    private String address2;
    private String address3;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private Boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
