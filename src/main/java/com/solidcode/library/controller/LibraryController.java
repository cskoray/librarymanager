package com.solidcode.library.controller;

import com.solidcode.library.dto.BookDetails;
import com.solidcode.library.exception.BookNotFoundException;
import com.solidcode.library.mapper.BookMapper;
import com.solidcode.library.repository.entity.Book;
import com.solidcode.library.service.CacheService;
import com.solidcode.library.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.solidcode.library.exception.ErrorType.BOOK_NOT_FOUND;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping("/v1/api/library")
@Validated
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;
    private final CacheService cache;
    private final BookMapper bookMapper;

    @GetMapping("/book/{isbn}")
    public ResponseEntity<BookDetails> getBookByISBN(@PathVariable String isbn) {

        return cache.getBook(isbn)
                .or(() -> libraryService.findBookByISBN(isbn))
                .map(book -> {
                    cache.addBook(book);
                    log.info("Book found: {}", book);
                    return ok(bookMapper.toDTO(book));
                })
                .orElseGet(() -> {
                    log.info("Book not found: {}", isbn);
                    return status(NOT_FOUND).build();
                });
    }

    @GetMapping("/books/author")
    public ResponseEntity<List<BookDetails>> getBooksByAuthor(@RequestParam String author) {

        List<BookDetails> books = libraryService.findBooksByAuthor(author).stream()
                .map(bookMapper::toDTO)
                .collect(toList());

        if (books.isEmpty()) {
            log.info("Books not found for author: {}", author);
            return status(NOT_FOUND).build();
        }

        return ok(books);
    }

    @PostMapping("/book")
    @ResponseStatus(CREATED)
    public void addBook(@Valid @RequestBody BookDetails bookDTO) {

        Book book = bookMapper.toEntity(bookDTO);

        libraryService.addBook(book);

        log.info("Book added: {}", book);
    }

    @PostMapping("/book/{isbn}/borrow")
    @ResponseStatus(OK)
    public void borrowBook(@PathVariable String isbn) {

        if (libraryService.borrowBook(isbn)) {

            cache.removeBook(isbn);

            log.info("Book borrowed: {}", isbn);
        } else {

            log.info("Book not found: {}", isbn);
            throw new BookNotFoundException(BOOK_NOT_FOUND, "isbn");
        }
    }

    @PostMapping("/book/{isbn}/return")
    @ResponseStatus(OK)
    public void returnBook(@PathVariable String isbn) {

        if (libraryService.returnBook(isbn)) {

            cache.removeBook(isbn);

            log.info("Book returned: {}", isbn);
        } else {

            log.info("Book not found: {}", isbn);

            throw new BookNotFoundException(BOOK_NOT_FOUND, "isbn");
        }
    }

    @DeleteMapping("/book/{isbn}")
    @ResponseStatus(OK)
    public void removeBook(@PathVariable String isbn) {

        libraryService.removeBook(isbn);

        cache.removeBook(isbn);

        log.info("Book removed: {}", isbn);
    }
}
