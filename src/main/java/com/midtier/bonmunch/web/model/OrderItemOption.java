package com.midtier.bonmunch.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemOption {

    private UUID orderItemOptionId;
    private UUID orderItemId;
    private UUID itemOptionId;


}
