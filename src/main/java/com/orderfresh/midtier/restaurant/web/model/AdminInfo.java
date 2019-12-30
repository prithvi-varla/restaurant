package com.orderfresh.midtier.restaurant.web.model;

import com.orderfresh.midtier.restaurant.model.DailySummay;
import com.orderfresh.midtier.restaurant.model.LastOrder;
import com.orderfresh.midtier.restaurant.model.MonthlySummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfo {

    DailySummay dailySummary;

    List<LastOrder> ordersSummary;

    List<MonthlySummary> monthlySummary;
}
