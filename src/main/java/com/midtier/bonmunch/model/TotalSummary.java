package com.midtier.bonmunch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TotalSummary {

    private String totalUsers;

    private String totalOrders;

    private String totalRevenue;

    private List<String> monthlyOrdersList;

}
