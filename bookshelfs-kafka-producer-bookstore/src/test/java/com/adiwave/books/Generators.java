package com.adiwave.books;

import java.util.UUID;

public class Generators {

    public UUID generateStaticUuid(char digit) {
        String hex = String.valueOf(digit).repeat(8) + "-" +
                String.valueOf(digit).repeat(4) + "-" +
                String.valueOf(digit).repeat(4) + "-" +
                String.valueOf(digit).repeat(4) + "-" +
                String.valueOf(digit).repeat(12);
        return UUID.fromString(hex);
    }
}
