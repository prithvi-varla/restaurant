package com.midtier.bonmunch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ImageResult {

    HttpStatus status;
    String[] keys;

    public ImageResult() {}

    public ImageResult(HttpStatus status, List<String> keys) {
        this.status = status;
        this.keys = keys == null ? new String[] {}: keys.toArray(new String[] {});

    }
}
