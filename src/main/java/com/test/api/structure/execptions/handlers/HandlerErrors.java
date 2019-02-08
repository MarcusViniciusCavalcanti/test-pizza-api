package com.test.api.structure.execptions.handlers;

import com.test.api.structure.execptions.InvalidParamsException;
import com.test.api.structure.execptions.ResponseErrors;
import com.test.api.structure.execptions.ValidationErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.Objects.*;

@ControllerAdvice
public class HandlerErrors {
    private final static Logger log = LoggerFactory.getLogger(HandlerErrors.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException exception, HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: " + exception.getClass().getSimpleName());
        ResponseErrors errors = ResponseErrors.Builder.newResponse()
                .withTitle("Resource not found")
                .withTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .withException(exception.getClass())
                .withErrors(exception.getMessage())
                .withStatusCode(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExist(EntityExistsException exception, HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: " + exception.getClass().getSimpleName());
        ResponseErrors errors = ResponseErrors.Builder.newResponse()
                .withTitle("Entity exist")
                .withTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .withException(exception.getClass())
                .withErrors(exception.getMessage())
                .withStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();

        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException exception, HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: " + exception.getClass().getSimpleName());
        ResponseErrors errors = ResponseErrors.Builder.newResponse()
                .withTitle("Duplicated value for column unique")
                .withTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .withException(exception.getClass())
                .withErrors(exception.getMessage())
                .withStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();

        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidParamsException.class)
    public ResponseEntity<?> handleInvalidParams(InvalidParamsException exception, HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: " + exception.getClass().getSimpleName());
        ResponseErrors errors = ResponseErrors.Builder.newResponse()
                .withTitle("Validation Errors " + exception.getTitle())
                .withTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .withException(exception.getClass())
                .withErrors(new ValidationErrors(exception.errors()))
                .withStatusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResultDataAccess(EmptyResultDataAccessException exception, HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: " + exception.getClass().getSimpleName());
        ResponseErrors errors = ResponseErrors.Builder.newResponse()
                .withTitle("Not possible delete entity")
                .withTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .withException(exception.getClass())
                .withErrors(requireNonNull(exception.getMessage()).replace("No class br.edu.utfpr.reclamaguarapuava.model.", ""))
                .withStatusCode(HttpStatus.GONE.value())
                .build();

        return new ResponseEntity<>(errors, HttpStatus.GONE);
    }
}