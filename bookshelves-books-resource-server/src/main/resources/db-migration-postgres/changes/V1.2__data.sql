SET search_path TO books;
-- AUTHORS
WITH authors_data AS (
    INSERT INTO books.authors (id, name, surname, nationality, birth_date)
        VALUES
            (gen_random_uuid(), 'George', 'Orwell', 'British', '1903-06-25'),
            (gen_random_uuid(), 'Jane', 'Austen', 'British', '1775-12-16'),
            (gen_random_uuid(), 'Mark', 'Twain', 'American', '1835-11-30'),
            (gen_random_uuid(), 'Haruki', 'Murakami', 'Japanese', '1949-01-12'),
            (gen_random_uuid(), 'Isabel', 'Allende', 'Chilean', '1942-08-02')
        RETURNING id
),

-- BOOKS
books_data AS (
 INSERT INTO books.books (id, title, edition, author_id)
     SELECT
         gen_random_uuid(),
         b.title,
         b.edition,
         a.id
     FROM authors_data a
              JOIN (
         VALUES
             ('1984', '1st Edition'),
             ('Pride and Prejudice', '3rd Edition'),
             ('Adventures of Huckleberry Finn', '2nd Edition'),
             ('Kafka on the Shore', '1st Edition'),
             ('The House of the Spirits', '4th Edition')
     ) AS b(title, edition)
                   ON TRUE
     LIMIT 5
     RETURNING id
)

-- ORDERS
INSERT INTO books.orders (id, order_date, books_id, price, qty)
SELECT
    gen_random_uuid(),
    d.order_date,
    b.id,
    d.price,
    d.qty
FROM books_data b
         JOIN (
    VALUES
        ('2025-01-05'::date, 19.99, 2),
        ('2025-01-08'::date, 14.50, 1),
        ('2025-01-12'::date, 22.00, 3),
        ('2025-01-15'::date, 18.75, 1),
        ('2025-01-18'::date, 25.00, 2)
) AS d(order_date, price, qty)
ON TRUE
LIMIT 5;
