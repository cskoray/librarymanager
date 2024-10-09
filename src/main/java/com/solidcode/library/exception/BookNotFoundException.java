package com.solidcode.library.exception;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {

    private ErrorType errorType;
    private String field;

    public BookNotFoundException(ErrorType errorType, String field) {

        super(errorType.getMessage());
        this.errorType = errorType;
        this.field = field;
    }
}