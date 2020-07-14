package com.smartbee.crm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class SystemExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SystemExceptionHandler.class);

    private final ObjectMapper mapper;

    public SystemExceptionHandler(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity dataNotFoundHandler(final DataNotFoundException e) {
        final String msg = e.getMessage();
        final Class type = e.getType();

        LOG.debug(msg, type.getSimpleName());

        final ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .type(type.getSimpleName())
                .message(e.getMessage())
                .build();

        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUUIDException.class)
    public ResponseEntity invalidUUIDException(final InvalidUUIDException e) {
        final String msg = e.getMessage();

        LOG.error(msg);

        final ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}

@Data
@Builder
class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String type;
    private String message;
}
