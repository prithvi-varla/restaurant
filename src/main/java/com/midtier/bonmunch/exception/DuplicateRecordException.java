package com.midtier.bonmunch.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class DuplicateRecordException extends RuntimeException {


    private List<String> errors;

    /**
     * Use this instance to throw list of {@link Error} with {@link Error#errorCode} and {@link Error#dataPath}.
     */
    public DuplicateRecordException(List<String> errors) {
        this.errors = errors;
    }

    public DuplicateRecordException(String... errors) {
        this.errors = Arrays.asList(errors);
    }

}
