package com.midtier.bonmunch.factory;

import com.midtier.bonmunch.web.model.Image;
import com.midtier.bonmunch.web.model.ImageType;
import com.midtier.bonmunch.web.model.ImageUI;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageDomainFactory {

    @Value("${image.location.url}")
    private String url;

    public ImageUI getUpdatedImage(com.midtier.bonmunch.model.Image image, ImageType imageType, ApiPrincipal apiPrincipal) {

        return ImageUI
                .builder()
                .id(image.getId())
                .companyId(image.getCompanyId())
                .name(image.getName())
                .srcUrl(url+apiPrincipal.getCompanyId()+"/"+imageType.toString()+"/"+image.getId())
                .build();
    }

    public Image getGalleryImages(List<ImageUI> imageUI) {


        Image galleryImages = new Image();
        galleryImages.setImages(imageUI);

        return galleryImages;
    }

}
