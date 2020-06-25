package com.midtier.bonmunch.dummy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private UUID id;
    private String firstname;
    private String lastname1;
    private int age1;

    private String session_token;
}
