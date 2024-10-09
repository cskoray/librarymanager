package com.solidcode.library.service;

import com.solidcode.library.repository.BookRepository;
import com.solidcode.library.repository.entity.Book;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final BookRepository bookRepository;

    @PostConstruct
    public void init() {

        Book book = new Book();
        book.setIsbn(UUID.randomUUID().toString());
        book.setTitle("The Lord of the Rings");
        book.setAuthor("J.R.R. Tolkien");
        book.setPublicationYear(1954);
        bookRepository.save(book);

        log.info("Default book added: {}", book);
    }
}
