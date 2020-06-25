package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.CompanyDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CompanyRepository extends ReactiveMongoRepository<CompanyDTO, UUID> {

    Mono<CompanyDTO> findByCompanyId(UUID companyId);
}
