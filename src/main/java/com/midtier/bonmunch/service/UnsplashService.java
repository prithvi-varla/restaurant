package com.midtier.bonmunch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.midtier.bonmunch.exception.UnsplashServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class UnsplashService {

    @Autowired
    @Qualifier("webClient")
    WebClient webClient;

    @Value("${config.profileservice.url}")
    String profileUrl;

    @Value("${config.profileservice.impl_url:#{null}}")
    private String userProfileServiceImplUrl;

    @Value("${config.profileservice.health_url}")
    private String userProfileServiceHealthUrl;


    public Mono<JsonNode> getUnsplash(
            String jwtUrl,
            Optional<String> authHeader
    ) {

        String url;

        if (jwtUrl != null && jwtUrl.contains("localhost")) {
            url = jwtUrl;
        } else {
            url = UriComponentsBuilder.fromUriString(jwtUrl)
                                      .port(443)
                                      .scheme("http")
                                      .build().toUriString();
        }

        WebClient.RequestHeadersSpec<?> request =
                webClient
                        .get()
                        .uri(url);
        if (authHeader.isPresent()) {
            request = request.header(HttpHeaders.AUTHORIZATION, authHeader.get());
        }


        return
                request
                        .exchange()
                        .flatMap(response -> getJsonNode(url, response));
    }

    private Mono<JsonNode> getJsonNode(String url, ClientResponse response) {

        if (response.statusCode() != HttpStatus.OK) {
            throw new UnsplashServiceException(response.statusCode(), url);
        }
        return response.bodyToMono(JsonNode.class);
    }
}
