package com.midtier.bonmunch.factory;

import com.midtier.bonmunch.web.model.Company;
import com.midtier.bonmunch.repository.model.CompanyDTO;
import com.midtier.bonmunch.web.model.Company;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CompanyDomainFactory {

    public CompanyDTO getCompanyDTOBuild(Company company) {
        return CompanyDTO.builder()
                         .companyId(company.getCompanyId())
                         .companyCode(company.getCompanyCode())
                         .companyName(company.getCompanyName())
                         .address1(company.getAddress1())
                         .address2(company.getAddress2())
                         .address3(company.getAddress3())
                         .country(company.getCountry())
                         .state(company.getState())
                         .city(company.getCity())
                         .postalCode(company.getPostalCode())
                         .active(company.getActive() == null ? Boolean.FALSE : company.getActive())
                         .createdDate(LocalDateTime.now())
                         .updatedDate(LocalDateTime.now())
                         .build();
    }

    public Company getCompanyBuild(CompanyDTO companyDTO) {
        return Company.builder()
                      .companyId(companyDTO.getCompanyId())
                      .companyCode(companyDTO.getCompanyCode())
                      .companyName(companyDTO.getCompanyName())
                      .address1(companyDTO.getAddress1())
                      .address2(companyDTO.getAddress2())
                      .address3(companyDTO.getAddress3())
                      .country(companyDTO.getCountry())
                      .state(companyDTO.getState())
                      .city(companyDTO.getCity())
                      .postalCode(companyDTO.getPostalCode())
                      .active(companyDTO.getActive())
                      .build();
    }
}
