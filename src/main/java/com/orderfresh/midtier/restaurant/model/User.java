package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class User {

    private UUID userId;

    private String companyId;

    private String first_name;

    private String last_name;

    private String email;

    private String password;

    private String session_token;
}
