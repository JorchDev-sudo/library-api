CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50)
);

CREATE TABLE authors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL
);

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(50) NOT NULL,
    publication_date TIMESTAMP NOT NULL,
    stock BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE author_books (
    author_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,

    PRIMARY KEY (author_id, book_id),

    CONSTRAINT fk_author_books_author
        FOREIGN KEY (author_id) REFERENCES authors(id),

    CONSTRAINT fk_author_books_book
        FOREIGN KEY (book_id) REFERENCES books(id)
);

CREATE TABLE loans (
    id BIGSERIAL PRIMARY KEY,

    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    loan_date TIMESTAMP NOT NULL,
    due_date TIMESTAMP,
    return_date TIMESTAMP,

    CONSTRAINT fk_loan_book
        FOREIGN KEY (book_id) REFERENCES books(id),

    CONSTRAINT fk_loan_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);
