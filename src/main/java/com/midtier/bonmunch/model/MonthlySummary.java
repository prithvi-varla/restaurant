package com.midtier.bonmunch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MonthlySummary {

    private String id;

    private String value;

    private String progress;

    private String difference;
}
