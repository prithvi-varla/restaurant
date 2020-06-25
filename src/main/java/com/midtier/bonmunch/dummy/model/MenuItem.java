package com.midtier.bonmunch.dummy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MenuItem {

    private String id;

    private String name;

    private float price;

    private String description;

    @JsonProperty("item_option_sections")
    private List<ItemOptionSection> itemOptionSection;
}
