package com.adiwave.books.service;

import com.adiwave.books.domain.Book;
import com.adiwave.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

// CRUD Operations
@Service
public class BookService {

    private BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    // return true if the book is valid
    public Boolean isValid(final Book book) {
        return book != null && !book.title().isEmpty();
    }

    // Get all records from the books table
    public Flux<Book> getAllBooks() {
        return this.repository.findAll();
    }


    // Save a new book record
    public Mono<Book> createBook(final Book book) {
        return this.repository.save(book);
    }

    // Update an existing book record
    @Transactional
    public Mono<Book> updateBook(final Book bookToUpdate) {
        return this.repository.findById(bookToUpdate.id())
                .flatMap(currentBook -> {
                    Book newBook = new Book(
                            currentBook.id(),
                            bookToUpdate.title(),
                            bookToUpdate.edition(),
                            bookToUpdate.authorId()
                    );
                    return this.repository.save(newBook);
                });
    }

    // Delete the task record by specified id
    @Transactional
    public Mono<Void> deleteBook(final UUID id) {
        return this.repository.findById(id)
                .flatMap(this.repository::delete);
    }
}
