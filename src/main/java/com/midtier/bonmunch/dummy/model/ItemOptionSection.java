package com.midtier.bonmunch.dummy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemOptionSection {

    private String id;

    private String name;

    private String description;

    private boolean required;

    private int min_allowed;

    private int max_allowed;

    private String created_at;

    private String updated_at;

    @JsonProperty("item_options")
    private List<ItemOption> itemOption;



}
