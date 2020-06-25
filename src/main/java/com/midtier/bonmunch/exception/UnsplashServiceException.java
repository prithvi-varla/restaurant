package com.midtier.bonmunch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public class UnsplashServiceException extends RuntimeException {
    private HttpStatus responseStatus;
    private String url;
}

