package com.midtier.bonmunch.dummy.model;

import com.midtier.bonmunch.model.DailySummay;
import com.midtier.bonmunch.model.LastOrder;
import com.midtier.bonmunch.model.MonthlySummary;
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
