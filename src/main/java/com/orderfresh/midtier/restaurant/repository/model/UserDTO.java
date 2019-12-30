package com.orderfresh.midtier.restaurant.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@Document(collection = "users")
public class UserDTO {

    @Id
    private UUID userId;
    private UUID companyId;
    private String emailAddress;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean active;
    private String level;
    private String roles;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
