package com.solidcode.library.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorType {

    BOOK_NOT_FOUND("10001", "Book cannot be found.", NOT_FOUND),
    DUPLICATE_BOOK("10002", "Book with ISBN already exists.", BAD_REQUEST),
    INVALID_FIELD("10003", "Invalid field.", BAD_REQUEST);

    private String code;
    private String message;
    private HttpStatus httpStatus;
}
