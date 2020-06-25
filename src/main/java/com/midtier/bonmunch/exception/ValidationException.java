package com.midtier.bonmunch.exception;

import com.midtier.bonmunch.model.Error;
import com.midtier.bonmunch.model.Error;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private List<Error> errors;

    private boolean externalServiceError;

    private List<String> externalServiceErrors;

    /**
     * Use this instance to throw a single field validation.
     *
     * @param dataPath  - Will be sent as {@link Error#dataPath} in error response
     * @param errorCode - Will be used to get corresponding error message from errors.properties
     */
    public ValidationException(String dataPath, String errorCode) {
        errors = Collections.singletonList(
                Error.builder()
                     .dataPath(dataPath)
                     .errorCode(errorCode)
                     .build()
        );
    }

    /**
     * Use this instance to throw errors captured during external service validations. Like profile service, EMT.
     *
     * @param externalServiceError - Based on this flag we decide whether list {@link Error} object needs to be
     *                             constructed.
     */

    public ValidationException(boolean externalServiceError, List<String> externalServiceErrors) {
        this.externalServiceError = externalServiceError;
        this.externalServiceErrors = externalServiceErrors;
    }

    /**
     * Use this instance to throw list of {@link Error} with {@link Error#errorCode} and {@link Error#dataPath}
     */
    public ValidationException(List<Error> errors) {
        this.errors = errors;
    }

}

