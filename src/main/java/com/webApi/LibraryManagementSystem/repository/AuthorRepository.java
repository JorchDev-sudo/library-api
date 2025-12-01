package com.webApi.LibraryManagementSystem.repository;

import com.webApi.LibraryManagementSystem.model.AuthorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorModel, Long> {
    AuthorModel findByName(String name);
}
