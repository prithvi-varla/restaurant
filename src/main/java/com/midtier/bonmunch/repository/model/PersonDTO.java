package com.midtier.bonmunch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "persons")
public class PersonDTO {

    @Id
    private UUID id;
    private String firstname;
    private String lastname;
    private int age;

    private UUID session_token;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;
}
