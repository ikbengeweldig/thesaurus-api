package com.thesaurus.infrastructure.in;

import com.thesaurus.api.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Optional.ofNullable;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {

        String message = ofNullable(ex).map(Throwable::getMessage)
                                       .filter(StringUtils::hasText)
                                       .orElse("internal server error");
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .error(message)
                                                   .build();
        return super.handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}