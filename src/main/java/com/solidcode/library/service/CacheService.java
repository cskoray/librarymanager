package com.solidcode.library.service;

import com.solidcode.library.repository.entity.Book;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Service
public class CacheService {

    private final Map<String, Book> cache = new ConcurrentHashMap<>();

    public Optional<Book> getBook(String isbn) {
        return ofNullable(cache.get(isbn));
    }

    public void addBook(Book book) {
        cache.put(book.getIsbn(), book);
    }

    public void removeBook(String isbn) {
        cache.remove(isbn);
    }
}
