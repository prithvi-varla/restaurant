package com.midtier.bonmunch.service;

import com.midtier.bonmunch.factory.CompanyDomainFactory;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.web.model.Company;
import com.midtier.bonmunch.exception.DuplicateRecordException;
import com.midtier.bonmunch.factory.CompanyDomainFactory;
import com.midtier.bonmunch.repository.dao.CompanyRepository;
import com.midtier.bonmunch.repository.model.CompanyDTO;
import com.midtier.bonmunch.web.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyDomainFactory companyDomainFactory;

    /*
        create new company information
     */
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
                "error.company.duplicate"));
    }

    private Mono<Company> createNewCompany(Company company) {
        CompanyDTO companyDTO = companyDomainFactory.getCompanyDTOBuild(company);
        return Mono.just(companyDTO).flatMap(companyRepository::save)
                                    .map(companyDTO1 -> companyDomainFactory.getCompanyBuild(companyDTO1));
    }

    /*
        Get Company information
     */
    public Mono<Company> getCompany(UUID companyId) {
        return companyRepository.findByCompanyId(companyId)
                                .publishOn(Schedulers.elastic())
                                .subscribeOn(Schedulers.parallel())
                                .map(companyDTO -> companyDomainFactory.getCompanyBuild(companyDTO));
    }

}
