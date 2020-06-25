package com.midtier.bonmunch.web.model;

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
public class Company  {

    private UUID companyId;
    private String companyCode;
    private String companyName;
    private String address1;
    private String address2;
    private String address3;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private Boolean active;
}
