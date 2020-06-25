package com.midtier.bonmunch.factory;

import com.midtier.bonmunch.web.model.SiteSetting;
import com.midtier.bonmunch.repository.model.SiteSettingDTO;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.web.model.SiteSetting;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SiteSettingDomainFactory {

    public SiteSettingDTO getSiteSettingDTOBuild(SiteSetting siteSetting, ApiPrincipal apiPrincipal) {

        return SiteSettingDTO.builder()
                          .siteSettingId(apiPrincipal.getCompanyId())
                          .companyId(apiPrincipal.getCompanyId())
                          .activeGallery(siteSetting.isActiveGallery())
                          .activeOrders(siteSetting.isActiveOrders())
                          .activeReviewPage(siteSetting.isActiveReviewPage())
                          .createdDate(LocalDateTime.now())
                          .updatedDate(LocalDateTime.now())
                          .build();
    }

    /*
            SiteSetting Domain changes
     */
    public SiteSetting getSiteSettingBuild(SiteSettingDTO siteSettingDTO) {
        return SiteSetting.builder()
                          .activeGallery(siteSettingDTO.isActiveGallery())
                          .activeOrders(siteSettingDTO.isActiveOrders())
                          .activeReviewPage(siteSettingDTO.isActiveReviewPage())
                          .build();
    }

    public SiteSettingDTO createEmptyObject(ApiPrincipal principal) {
        return SiteSettingDTO.builder()
                             .siteSettingId(UUID.randomUUID())
                             .companyId(principal.getCompanyId())
                             .build();
    }

}
