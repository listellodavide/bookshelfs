CREATE SCHEMA books;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE books.authors (
    id UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    surname VARCHAR NOT NULL,
    nationality VARCHAR NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE books.books (
    id UUID PRIMARY KEY,
    title VARCHAR NOT NULL,
    edition VARCHAR NOT NULL,
    author_id UUID NOT NULL REFERENCES books.authors(id)
);

CREATE TABLE books.orders (
    id UUID PRIMARY KEY,
    order_date DATE NOT NULL,
    books_id UUID NOT NULL REFERENCES books.books(id),
    price NUMERIC NOT NULL,
    qty INT NOT NULL
);