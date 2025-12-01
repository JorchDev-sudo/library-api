package com.webApi.LibraryManagementSystem.repository;

import com.webApi.LibraryManagementSystem.model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Long> {
    List<BookModel> findByTitle(String title);
}
