package com.midtier.bonmunch.dummy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemOption {

    private String id;

    private String item_option_section_id;

    private String name;

    private String description;

    private float price;

    private String created_at;

    private String updated_at;
}
