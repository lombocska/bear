package io.lombocska.bear.web.advice;

import io.lombocska.bear.web.errors.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<?> handleSerdesException(
            EmptyResultDataAccessException exception) {
        var apiException =
                ApiError.builder()
                        .type("entity_not_found")
                        .message(exception.getLocalizedMessage())
                        .build();

        return new ResponseEntity(apiException, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception exception) {
        log.error("Unexpected internal server error: ", exception);
        var apiException =
                ApiError.builder()
                        .type("internal_server_error")
                        .message("An unexpected internal server error occurred")
                        .build();
        return new ResponseEntity(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
