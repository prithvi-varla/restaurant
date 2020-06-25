package com.midtier.bonmunch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

    @Bean("webClient")
    public WebClient webClientWithContext() {

        HttpClient httpClient =
                HttpClient.create();

        ClientHttpConnector connector =
                new ReactorClientHttpConnector(httpClient);

        WebClient webClient =
                WebClient.builder()
                         .clientConnector(connector)
                         .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                         .build();
        return webClient;
    }
}
