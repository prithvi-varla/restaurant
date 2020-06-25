package com.midtier.bonmunch.service;

import com.midtier.bonmunch.web.model.SiteSetting;
import com.midtier.bonmunch.factory.SiteSettingDomainFactory;
import com.midtier.bonmunch.repository.dao.SettingRepository;
import com.midtier.bonmunch.repository.model.SiteSettingDTO;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SettingService {

    @Autowired
    SiteSettingDomainFactory siteSettingFactory;

    @Autowired
    SettingRepository settingRepository;


    public Mono<SiteSetting> createSiteSetting(Mono<SiteSetting> siteSettingMono, ApiPrincipal apiPrincipal) {

        return
                siteSettingMono.map(siteSettingObject -> siteSettingFactory.getSiteSettingDTOBuild(siteSettingObject, apiPrincipal))
                        .publishOn(Schedulers.elastic())
                        .flatMap(settingRepository::save)
                        .publishOn(Schedulers.elastic())
                        .subscribeOn(Schedulers.parallel())
                        .map(siteSettingDTOObject -> siteSettingFactory.getSiteSettingBuild(siteSettingDTOObject));


    }

    public Mono<SiteSetting> getSiteSetting(ApiPrincipal apiPrincipal) {

        return
                settingRepository.findByCompanyId(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .map(siteSettingDTOObject -> siteSettingFactory.getSiteSettingBuild(siteSettingDTOObject))
                                .switchIfEmpty(Mono.defer(() -> Mono.just(new SiteSetting())));
    }

    private Mono<SiteSetting> createNewUser(ApiPrincipal principal) {
        SiteSettingDTO siteSettingDTO = siteSettingFactory.createEmptyObject(principal);
        return Mono.just(siteSettingDTO).flatMap(settingRepository::save)
                   .publishOn(Schedulers.elastic())
                   .subscribeOn(Schedulers.parallel())
                   .map(siteSettingDTOObject -> siteSettingFactory.getSiteSettingBuild(siteSettingDTOObject));

    }
}
