package com.midtier.bonmunch.dummy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MonthlyActivity {

    private String totalUsers;

    private String totalRevenue;

    private List<String> monthlyOrdersChart;

    private String orders;



}
