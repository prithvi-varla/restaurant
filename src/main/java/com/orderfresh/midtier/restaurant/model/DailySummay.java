package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DailySummay {

    private String id;

    private String name;

    private String users;

    private String visits;

    private String orders;
}
