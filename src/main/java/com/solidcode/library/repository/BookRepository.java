package com.solidcode.library.repository;

import com.solidcode.library.repository.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Lock(PESSIMISTIC_WRITE)
    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorIgnoreCase(String author);
}
