package com.orderfresh.midtier.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RestaurantAdminInfo {

    DailySummay dailySummary;

    List<LastOrder> ordersSummary;

    List<MonthlySummary> monthlySummary;

}
