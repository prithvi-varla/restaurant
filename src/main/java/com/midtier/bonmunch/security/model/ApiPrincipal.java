package com.midtier.bonmunch.security.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ApiPrincipal {

    private UUID userId;
    private String entityId;
    private UUID companyId;
    private JsonNode profile;
    private JsonNode jwt;
    private List<String> scopes;
    private String iss;
    private String userName;


    public boolean hasScope(String scope) {
        boolean result = false;
        if (scopes != null) {
            result = scopes.contains("*") || scopes.contains(scope);
        }
        return result;
    }
}
