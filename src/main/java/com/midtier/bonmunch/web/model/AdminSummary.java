package com.midtier.bonmunch.web.model;

import com.midtier.bonmunch.model.DailySummay;
import com.midtier.bonmunch.model.LastOrder;
import com.midtier.bonmunch.model.MonthlySummary;
import com.midtier.bonmunch.model.TotalSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminSummary {

    List<MonthlySummary> monthlySummary;

    DailySummay dailySummary;

    TotalSummary totalSummary;

    List<LastOrder> ordersSummary;
}
