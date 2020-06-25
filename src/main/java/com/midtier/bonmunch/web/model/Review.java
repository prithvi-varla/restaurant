package com.midtier.bonmunch.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Review {

    private UUID reviewId;
    private UUID companyId;
    private String name;
    private String phoneNumber;
    private String emailId;
    private String review;
}
