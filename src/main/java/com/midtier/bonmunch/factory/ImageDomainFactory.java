package com.midtier.bonmunch.factory;

import com.midtier.bonmunch.web.model.Image;
import com.midtier.bonmunch.web.model.ImageType;
import com.midtier.bonmunch.web.model.ImageUI;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ImageDomainFactory {

    @Value("${image.location.url}")
    private String url;

    public ImageUI getUpdatedImage(com.midtier.bonmunch.model.Image image, ImageType imageType, UUID companyId) {

        return ImageUI
                .builder()
                .id(image.getId())
                .companyId(image.getCompanyId())
                .imageName(image.getImageName())
                .imageDescription(image.getImageDescription())
                .buttonName(image.getButtonName())
                .buttonUri(image.getButtonUri())
                .srcUrl(url+companyId+"/"+imageType.toString()+"/"+image.getId())
                .build();
    }

    public Image getGalleryImages(List<ImageUI> imageUI) {


        Image galleryImages = new Image();
        galleryImages.setImages(imageUI);

        return galleryImages;
    }

}
