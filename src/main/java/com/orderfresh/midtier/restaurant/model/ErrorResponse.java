package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ErrorResponse {
    private List<Error> errors;
}

