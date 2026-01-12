package com.jorchdev.library_api.repositories;

import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthors(Author author, Pageable pageable);
}
