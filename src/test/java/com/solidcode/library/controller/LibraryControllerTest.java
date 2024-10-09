package com.solidcode.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solidcode.library.dto.BookDetails;
import com.solidcode.library.repository.BookRepository;
import com.solidcode.library.repository.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddBook() throws Exception {
        BookDetails book = new BookDetails("9781234567897", "Some Book", "Test Author", 2020, 5);

        mockMvc.perform(post("/v1/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        assertTrue(bookRepository.existsById("9781234567897"));
    }

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void testBorrowBookSuccess() throws Exception {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 5);
        bookRepository.save(book);

        mockMvc.perform(post("/v1/api/library/book/1234567890/borrow"))
                .andExpect(status().isOk());

        Book updatedBook = bookRepository.findById("1234567890").get();
        assertEquals(4, updatedBook.getAvailableCopies());
    }

    @Test
    void testReturnBookSuccess() throws Exception {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 4);
        bookRepository.save(book);

        mockMvc.perform(post("/v1/api/library/book/1234567890/return"))
                .andExpect(status().isOk());

        Book updatedBook = bookRepository.findById("1234567890").get();
        assertEquals(5, updatedBook.getAvailableCopies());
    }

    @Test
    void testBorrowBookWhenNoCopiesLeft() throws Exception {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 0);
        bookRepository.save(book);

        mockMvc.perform(post("/v1/api/library/book/1234567890/borrow"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveBook() throws Exception {
        Book book = new Book("1234567890", "Test Book", "Test Author", 2020, 5);
        bookRepository.save(book);

        mockMvc.perform(delete("/v1/api/library/book/1234567890"))
                .andExpect(status().isOk());

        assertFalse(bookRepository.existsById("1234567890"));
    }
}