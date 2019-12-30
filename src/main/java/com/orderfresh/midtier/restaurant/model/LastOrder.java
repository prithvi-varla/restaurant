package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LastOrder {

    private String id;

    private String date;

    private String name;

    private String status;

    private String price;

}
