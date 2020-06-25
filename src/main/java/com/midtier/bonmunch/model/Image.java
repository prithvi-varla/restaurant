package com.midtier.bonmunch.model;

import com.midtier.bonmunch.web.model.ImageType;
import com.midtier.bonmunch.web.model.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Image {

    @Id
    private UUID id;

    private UUID companyId;

    private ImageType imageType;

    private String name;
}
