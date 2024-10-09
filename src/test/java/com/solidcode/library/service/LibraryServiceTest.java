package com.solidcode.library.service;

import com.solidcode.library.exception.BookAlreadyExistsException;
import com.solidcode.library.repository.BookRepository;
import com.solidcode.library.repository.entity.Book;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    void testAddBook() {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 5);

        libraryService.addBook(book);

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testAddExistingBookThrowsException() {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 5);

        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));

        assertThrows(BookAlreadyExistsException.class, () -> libraryService.addBook(book));
    }

    @Test
    void testBorrowBookSuccess() {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 5);

        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));

        boolean result = libraryService.borrowBook("1234567890");

        assertTrue(result);
        assertEquals(4, book.getAvailableCopies());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testBorrowBookNoAvailableCopies() {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 0);

        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));

        boolean result = libraryService.borrowBook("1234567890");

        assertFalse(result);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testReturnBookSuccess() {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 4);

        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));

        boolean result = libraryService.returnBook("1234567890");

        assertTrue(result);
        assertEquals(5, book.getAvailableCopies());
        verify(bookRepository, times(1)).save(book);
    }

    @RepeatedTest(2)
    void testBorrowBookConcurrency() throws InterruptedException {
        String isbn = "1234567890";
        Book book = new Book(isbn, "Concurrent Book", "Concurrent Author", 2020, 10);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    libraryService.borrowBook(isbn);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        verify(bookRepository, times(10)).save(any(Book.class));
    }
}