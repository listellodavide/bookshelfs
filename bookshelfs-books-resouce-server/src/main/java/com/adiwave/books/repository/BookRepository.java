package com.adiwave.books.repository;

import com.adiwave.books.domain.Book;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface BookRepository extends R2dbcRepository<Book, UUID> {

    Flux<Book> findByTitle(String title);

    Flux<Book> findByAuthorId(UUID authorId);
}
