package org.example.field_mask_demo.beans;

import lombok.extern.slf4j.Slf4j;
import org.example.field_mask_demo.commons.exception.BadRequestException;
import org.example.field_mask_demo.commons.exception.NotFoundException;
import org.example.field_mask_demo.commons.exception.ServerException;
import org.example.pb.ErrorResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String SERVER_EXCEPTION = "Server exception";

    private static ErrorResponse error(String serverException) {
        return ErrorResponse.newBuilder().setError(serverException).build();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> serverException(Exception exception) {
        log.error(SERVER_EXCEPTION, exception);
        return ResponseEntity.internalServerError().body(error(SERVER_EXCEPTION));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> badRequest(BadRequestException badRequestException) {
        return ResponseEntity.badRequest().body(error(badRequestException.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> internalServerError(ServerException serverException) {
        log.error(SERVER_EXCEPTION, serverException);
        return ResponseEntity.internalServerError().body(error(serverException.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorResponse> notFound(Exception exception) {
        if (exception instanceof NotFoundException) {
            return new ResponseEntity<>(error(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(error("Resource not found"), HttpStatus.NOT_FOUND);
    }
}
