package com.orderfresh.midtier.restaurant.service;

import com.orderfresh.midtier.restaurant.exception.DuplicateRecordException;
import com.orderfresh.midtier.restaurant.factory.CompanyDomainFactory;
import com.orderfresh.midtier.restaurant.repository.dao.CompanyRepository;
import com.orderfresh.midtier.restaurant.repository.model.CompanyDTO;
import com.orderfresh.midtier.restaurant.web.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyDomainFactory companyDomainFactory;

    public Mono<Company> createCompany(Mono<Company> companyMono) {

        return companyMono.flatMap(company -> getCompanyMono(company));
    }

    private Mono<Company> getCompanyMono(Company company) {
        return companyRepository.findByCompanyId(companyDomainFactory
                                                         .getCompanyDTOBuild(company)
                                                         .getCompanyId())
                                .publishOn(Schedulers.elastic())
                                .subscribeOn(Schedulers.parallel())
                                .map(d -> companyDomainFactory.getCompanyBuild(d))
                                .flatMap (companyObject -> getError())
                                .switchIfEmpty(Mono.defer(() -> createNewCompany(company)));
    }

    private Mono<Company> getError() {
        return Mono.error(new DuplicateRecordException(
                "error.restaurant.company.duplicate"));
    }

    private Mono<Company> createNewCompany(Company company) {
        CompanyDTO companyDTO = companyDomainFactory.getCompanyDTOBuild(company);
        return Mono.just(companyDTO).flatMap(companyRepository::save)
                                    .map(companyDTO1 -> companyDomainFactory.getCompanyBuild(companyDTO1));
    }
}
