package com.adiwave.books.repository;

import com.adiwave.books.domain.Author;
import com.adiwave.books.domain.Book;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthorRepository extends R2dbcRepository<Author, UUID> {

    Mono<Author> findByNameAndSurname(String name, String surname);

    Mono<Void> deleteByNameAndSurname(String name, String surname);


    @Query("""
        SELECT a.*
        FROM authors a
        JOIN books b ON a.id = b.author_id
        WHERE b.title = :title
    """)
    Mono<Author> findAuthorByBookTitle(String title);

}
