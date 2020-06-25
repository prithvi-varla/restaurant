package com.midtier.bonmunch.exception;

import static java.util.stream.Collectors.toList;

import com.midtier.bonmunch.model.Error;
import com.midtier.bonmunch.model.ErrorResponse;
import com.midtier.bonmunch.model.Error;
import com.midtier.bonmunch.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String SLASH = "/";

    private static final String DEFAULT_ERROR_CODE = "errorProcessingRequest";

    private static final String UNAUTHORIZED_ERROR_CODE = "unauthorizedRequest";

    @Autowired
    MessageSource errorMessageSource;

    @Autowired
    MessageSource emtErrorMessageSource;

    private MessageSourceAccessor errorMessageAccessor;
    private MessageSourceAccessor emtErrorCodeAccessor;

    private Map<String, MessageSourceAccessor> accessorMap;


    @PostConstruct
    private void init() {
        errorMessageAccessor = new MessageSourceAccessor(errorMessageSource, Locale.ENGLISH);
        emtErrorCodeAccessor = new MessageSourceAccessor(emtErrorMessageSource, Locale.ENGLISH);
    }

    /**
     * Handle the validation exception.
     *
     * @param exception the validation exception that needs to be handled
     * @return the response entity with the corresponding error message
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(ValidationException exception) {
        List<Error> errors;
        if (!exception.isExternalServiceError()) {
            errors =
                    exception.getErrors()
                             .stream()
                             .map(error -> error.toBuilder()
                                                .errorMessage(
                                                        errorMessageAccessor.getMessage(
                                                                error.getErrorCode(),
                                                                error.getErrorCode()
                                                        )
                                                )
                                                .build()
                             )
                             .collect(toList());


        } else {
            errors = new ArrayList<>(exception.getExternalServiceErrors().size());
            exception.getExternalServiceErrors()
                     .stream()
                     .forEach(emtErrorCode -> {
                         Error error = Error.builder()
                                            .errorCode(emtErrorCodeAccessor.getMessage(emtErrorCode))
                                            .errorMessage(errorMessageAccessor.getMessage(emtErrorCodeAccessor
                                                                                                  .getMessage(
                                                                                                          emtErrorCode)))
                                            .build();
                         errors.add(error);
                     });

        }

        ErrorResponse errorResponse = new ErrorResponse(errors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle the generic exception.
     *
     * @param exception the generic exception that needs to be handled
     * @return the response entity with the corresponding error message
     */
    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getServerErrorResponse());
    }*/

    /**
     * This is the handler for resource not found exceptions.
     *
     * @param exception the exception to be handled
     * @return response entity with the error message and http status
     * @see ResourceNotFoundException
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException exception) {
        List<Error> errors;
        errors = new ArrayList<>(exception.getErrors().size());
        exception.getErrors()
                 .stream()
                 .forEach(emtError -> {
                     Error error = Error.builder()
                                        .errorCode(emtErrorCodeAccessor.getMessage(emtError))
                                        .errorMessage(errorMessageAccessor.getMessage(emtErrorCodeAccessor.getMessage(
                                                emtError)))
                                        .build();
                     errors.add(error);
                 });

        ErrorResponse errorResponse = new ErrorResponse(errors);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * This is the handler for resource not found exceptions.
     *
     * @param exception the exception to be handled
     * @return response entity with the error message and http status
     * @see DuplicateRecordException
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleException(DuplicateRecordException exception) {
        List<Error> errors;
        errors = new ArrayList<>(exception.getErrors().size());
        exception.getErrors()
                 .stream()
                 .forEach(emtError -> {
                     Error error = Error.builder()
                                        .errorCode(emtErrorCodeAccessor.getMessage(emtError))
                                        .errorMessage(errorMessageAccessor.getMessage(emtErrorCodeAccessor.getMessage(
                                                emtError)))
                                        .build();
                     errors.add(error);
                 });

        ErrorResponse errorResponse = new ErrorResponse(errors);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    private ErrorResponse getServerErrorResponse() {
        new ArrayList<>();
        List<Error> errors = new ArrayList<>(Arrays.asList(Error.builder()
                                                                .errorCode(DEFAULT_ERROR_CODE)
                                                                .build()));

        ErrorResponse errorResponse = new ErrorResponse(errors);

        return errorResponse;
    }


}
