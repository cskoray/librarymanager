package com.solidcode.library.service;

import com.solidcode.library.exception.BookAlreadyExistsException;
import com.solidcode.library.exception.BookNotFoundException;
import com.solidcode.library.repository.BookRepository;
import com.solidcode.library.repository.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.solidcode.library.exception.ErrorType.BOOK_NOT_FOUND;
import static com.solidcode.library.exception.ErrorType.DUPLICATE_BOOK;
import static java.util.Optional.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;

    @Transactional
    public synchronized void addBook(Book book) {

        log.info("Adding book: {}", book);
        bookRepository.findByIsbn(book.getIsbn())
                .ifPresentOrElse(existingBook -> {
                    log.info("Book already exists: {}", book.getIsbn());
                    throw new BookAlreadyExistsException(DUPLICATE_BOOK, "isbn");
                }, () -> bookRepository.save(book));
    }

    @Transactional
    public synchronized void removeBook(String isbn) {

        of(isbn).filter(bookRepository::existsById)
                .ifPresentOrElse(id -> {
                    bookRepository.deleteById(id);
                    log.info("Book removed: {}", id);
                }, () -> {
                    log.info("Book not found: {}", isbn);
                    throw new BookNotFoundException(BOOK_NOT_FOUND, null);
                });
    }

    public Optional<Book> findBookByISBN(String isbn) {

        return bookRepository.findById(isbn);
    }

    public List<Book> findBooksByAuthor(String author) {

        return bookRepository.findByAuthorIgnoreCase(author);
    }

    @Transactional
    public synchronized boolean borrowBook(String isbn) {

        return bookRepository.findByIsbn(isbn)
                .filter(book -> book.getAvailableCopies() > 0)
                .map(book -> {
                    book.setAvailableCopies(book.getAvailableCopies() - 1);
                    bookRepository.save(book);
                    log.info("Book borrowed: {}", isbn);
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Book not found or no available copies: {}", isbn);
                    return false;
                });
    }

    @Transactional
    public boolean returnBook(String isbn) {

        return bookRepository.findByIsbn(isbn)
                .map(book -> {
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                    bookRepository.save(book);
                    log.info("Book returned: {}", isbn);
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Book not found: {}", isbn);
                    return false;
                });
    }
}
