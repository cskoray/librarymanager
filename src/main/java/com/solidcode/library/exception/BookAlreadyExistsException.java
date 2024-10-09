package com.solidcode.library.exception;

public class BookAlreadyExistsException extends RuntimeException {

    private ErrorType errorType;
    private String field;

    public BookAlreadyExistsException(ErrorType errorType, String field) {

        super(errorType.getMessage());
        this.errorType = errorType;
        this.field = field;
    }
}