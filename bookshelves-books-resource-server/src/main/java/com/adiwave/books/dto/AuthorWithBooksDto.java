package com.adiwave.books.dto;

import com.adiwave.books.domain.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AuthorWithBooksDto(
        UUID id,
        String name,
        String surname,
        String nationality,
        LocalDate birthday,
        List<Book> books
) {}
