package com.adiwave.books.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("authors")
public record Author(
        @Id
        UUID id,
        String name,
        String surname,
        String nationality,
        LocalDate birthday
) {}
