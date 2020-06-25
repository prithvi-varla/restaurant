package com.midtier.bonmunch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DailySummay {

    private String monthlyUsers;

    private String usersProgress;

    private String totalUsers;

    private String monthlyOrders;

    private String ordersProgress;

    private String totalOrders;
}
