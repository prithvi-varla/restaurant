package com.orderfresh.midtier.restaurant.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    private String errorCode;

    private String errorMessage;

    private String dataPath;

    private String schemaPath;
}
