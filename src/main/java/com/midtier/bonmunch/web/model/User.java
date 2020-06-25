package com.midtier.bonmunch.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User  {

    private UUID userId;
    private UUID companyId;
    private String emailAddress;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String level;
    private Boolean active;
    private String roles;
}
