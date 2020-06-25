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
@Document(collection = "Reviews")
public class ReviewDTO {

    @Id
    private UUID reviewId;
    private UUID companyId;
    private String name;
    private String phoneNumber;
    private String emailId;
    private String review;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}

