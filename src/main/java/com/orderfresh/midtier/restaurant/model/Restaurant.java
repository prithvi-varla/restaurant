package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Restaurant {

    private String id;

    private String name;

    private String address;

    private String phone;

    private String img_url;

    private String cuisine;

    private Float delivery_minimum;

    private Float delivery_fee;

    private Float distance;

    private String rating_avg;

    private String rating_count;

    private Float latitude;

    private Float longitude;

}
