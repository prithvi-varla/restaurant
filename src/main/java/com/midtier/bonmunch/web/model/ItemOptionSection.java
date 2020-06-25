package com.midtier.bonmunch.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemOptionSection {

    private UUID itemOptionSectionId;
    private UUID menuItemId;
    private String name;
    private String description;
    private boolean required;
    private String minAllowed;
    private String maxAllowed;
    @JsonProperty("itemOptions")
    private List<ItemOption> itemOption;

}
