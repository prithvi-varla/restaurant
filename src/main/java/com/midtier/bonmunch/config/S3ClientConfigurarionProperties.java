package com.midtier.bonmunch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "aws")
@Data
public class S3ClientConfigurarionProperties {

    private Region region = Region.US_EAST_1;
    private URI endpoint = null;

    private String keyId;
    private String accessKey;
    private String bucket;

    // AWS S3 requires that file parts must have at least 5MB, except
    // for the last part. This may change for other S3-compatible services, so let't
    // define a configuration property for that
    private int multipartMinPartSize = 5*1024*1024;

}
