package com.adiwave.books.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("books")
public record Book(
        @Id
        UUID id,
        String title,
        String edition,
        UUID authorId
) {
}
