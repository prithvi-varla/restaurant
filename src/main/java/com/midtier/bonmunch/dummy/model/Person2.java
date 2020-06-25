package com.midtier.bonmunch.dummy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * An entity to represent a Person.
 *
 * @author prithvi
 */
@Data
@Builder
@AllArgsConstructor
@Document(collection = "persons")
public class Person2 {

    @Id
    private UUID id;
    private String firstname;
    private String lastname;
    private int age;
}
