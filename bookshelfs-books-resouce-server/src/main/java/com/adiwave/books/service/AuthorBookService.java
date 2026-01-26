package com.adiwave.books.service;

import com.adiwave.books.domain.Author;
import com.adiwave.books.domain.Book;
import com.adiwave.books.dto.AuthorWithBooksDto;
import com.adiwave.books.repository.AuthorRepository;
import com.adiwave.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// do the equivalent of JPA/ORM using Reactive Programming with R2DBC
@Service
public class AuthorBookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorBookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // one author for books with the same title
    public Mono<Author> findAuthorByBookTitle(String title) {
        return bookRepository.findByTitle(title)
                .next() // take first matching book title
                .flatMap(book -> authorRepository.findById(book.authorId()));
    }

    // multiple authors for books with the same title
    public Flux<Author> findAuthorsByBookTitle(String title) {
        return bookRepository.findByTitle(title)
                .map(Book::authorId)
                .distinct()
                .flatMap(authorRepository::findById);
    }

    // return all information about all the version of a book given the author name and surname
    public Flux<Book> findBooksByAuthorId(String name, String surname) {
        return authorRepository.findByNameAndSurname(name, surname)
                .flatMapMany(author -> bookRepository.findByAuthorId(author.id()));
    }

    public Mono<AuthorWithBooksDto> findAuthorWithBooksByTitle(String title) {
        return bookRepository.findByTitle(title)
                .next()
                .flatMap(book ->
                        authorRepository.findById(book.authorId())
                                .flatMap(author ->
                                        bookRepository.findByAuthorId(author.id())
                                                .collectList()
                                                .map(books ->
                                                        new AuthorWithBooksDto(
                                                                author.id(),
                                                                author.name(),
                                                                author.surname(),
                                                                author.nationality(),
                                                                author.birthday(),
                                                                books
                                                        )
                                                )
                                )
                );
    }

}
