package com.solidcode.library.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

import static com.solidcode.library.exception.ErrorType.DUPLICATE_BOOK;
import static com.solidcode.library.exception.ErrorType.INVALID_FIELD;
import static java.util.Collections.singletonList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorList> handleDataIntegrityViolationException(Exception exception) {
        return createSingleErrorListResponse(DUPLICATE_BOOK, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorList> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String field = exception.getBindingResult().getFieldErrors().get(0).getField();
        return createSingleErrorListResponse(INVALID_FIELD, field);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorList> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().toList().get(0);
        Error error = new Error(constraintViolation.getInvalidValue()
                .toString(), "10003", constraintViolation.getMessage());
        return new ResponseEntity<>(createErrorList(singletonList(error)), BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorList> handleBookNotFoundException(BookNotFoundException exception) {
        return createSingleErrorListResponse(exception.getErrorType(), exception.getField());
    }

    private ResponseEntity<ErrorList> createSingleErrorListResponse(ErrorType errorType, String field) {
        Error error = new Error(field, errorType.getCode(), errorType.getMessage());
        return new ResponseEntity<>(createErrorList(singletonList(error)), errorType.getHttpStatus());
    }

    private ErrorList createErrorList(List<Error> errorList) {
        return new ErrorList(errorList);
    }
}