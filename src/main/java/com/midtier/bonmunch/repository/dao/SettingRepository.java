package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.SiteSettingDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface SettingRepository extends ReactiveMongoRepository<SiteSettingDTO, UUID> {

    Mono<SiteSettingDTO> findByCompanyId(UUID companyId);
}
