package com.faf.project.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // =====================================================
    // VALIDATION ERROR
    // =====================================================

    @ExceptionHandler(
        MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> validationErrors =
                new HashMap<>();


        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                    validationErrors.put(
                        error.getField(),
                        error.getDefaultMessage()
                    )
                );


        Map<String, Object> response =
                new HashMap<>();


        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                400
        );

        response.put(
                "error",
                "Validation Error"
        );

        response.put(
                "message",
                validationErrors
        );

        response.put(
                "path",
                request.getRequestURI()
        );


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // =====================================================
    // INVALID JSON
    // =====================================================

    @ExceptionHandler(
        HttpMessageNotReadableException.class
    )
    public ResponseEntity<ErrorResponse>
    handleInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse(
                    LocalDateTime.now(),
                    400,
                    "Bad Request",
                    "Invalid JSON request",
                    request.getRequestURI()
                );


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // =====================================================
    // DUPLICATE DATA
    // =====================================================

    @ExceptionHandler(
        DataIntegrityViolationException.class
    )
    public ResponseEntity<ErrorResponse>
    handleDuplicateData(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse(
                    LocalDateTime.now(),
                    409,
                    "Conflict",
                    "Duplicate or conflicting data already exists",
                    request.getRequestURI()
                );


        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }


    // =====================================================
    // RESOURCE NOT FOUND
    // =====================================================

    @ExceptionHandler(
        ResourceNotFoundException.class
    )
    public ResponseEntity<ErrorResponse>
    handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse(
                    LocalDateTime.now(),
                    404,
                    "Not Found",
                    ex.getMessage(),
                    request.getRequestURI()
                );


        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    // =====================================================
    // RESPONSE STATUS EXCEPTION
    // =====================================================

    @ExceptionHandler(
        ResponseStatusException.class
    )
    public ResponseEntity<ErrorResponse>
    handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request) {

        int status =
                ex.getStatusCode().value();


        ErrorResponse response =
                new ErrorResponse(
                    LocalDateTime.now(),
                    status,
                    ex.getStatusCode().toString(),
                    ex.getReason(),
                    request.getRequestURI()
                );


        return ResponseEntity
                .status(status)
                .body(response);
    }


    // =====================================================
    // GENERAL EXCEPTION
    // =====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
    handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse(
                    LocalDateTime.now(),
                    500,
                    "Internal Server Error",
                    "Something went wrong. Please try again later.",
                    request.getRequestURI()
                );


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}