package com.adiwave.books.exercise;

public record Transaction(
        String currency,
        double amount,
        String userId
) {
}
