INSERT INTO users (id, email, name, password, role)
VALUES (
  1,
  'admin@library.com',
  'Admin',
  '$2a$12$FIu2c.s55mWsMyO/Gcf74.KAeFuCoeDnGHd/s9pBgDcwefPBKxqli',
  'LIBRARIAN'
);

INSERT INTO authors (id, name) VALUES (1, 'Gabriel García Márquez');
INSERT INTO authors (id, name) VALUES (2, 'J. R. R. Tolkien');
INSERT INTO authors (id, name) VALUES (3, 'George Orwell');
INSERT INTO authors (id, name) VALUES (4, 'Isaac Asimov');
INSERT INTO authors (id, name) VALUES (5, 'Stephen King');
INSERT INTO authors (id, name) VALUES (6, 'Jane Austen');

INSERT INTO books (id, title, publication_date, stock, isbn)
VALUES (1, 'Cien años de soledad', '1967-05-30T00:00:00', 10, '9780060929794');

INSERT INTO books (id, title, publication_date, stock, isbn)
VALUES (2, 'El amor en los tiempos del cólera', '1985-09-05T00:00:00', 10, '9780140255782');

INSERT INTO books (id, title, publication_date, stock, isbn)
VALUES (3, '1984', '1949-06-08T00:00:00', 10, '9780155658110');

INSERT INTO books (id, title, publication_date, stock, isbn)
VALUES (4, 'Rebelión en la granja', '1945-08-17T00:00:00', 10, '9780194267533');

INSERT INTO books (id, title, publication_date, stock, isbn)
VALUES (5, 'El señor de los anillos', '1954-07-29T00:00:00', 10, '9788845292613');

INSERT INTO books (id, title, publication_date, stock, isbn)
VALUES (6, 'La comunidad del anillo', '1954-07-29T00:00:00', 10, '9788445071793');

INSERT INTO author_books (author_id, book_id)
VALUES (1, 1);
INSERT INTO author_books (author_id, book_id)
VALUES (1, 2);
INSERT INTO author_books (author_id, book_id)
VALUES (3, 3);
INSERT INTO author_books (author_id, book_id)
VALUES (3, 4);
INSERT INTO author_books (author_id, book_id)
VALUES (2, 5);
INSERT INTO author_books (author_id, book_id)
VALUES (2, 6);

