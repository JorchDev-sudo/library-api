package com.webApi.LibraryManagementSystem.repository;

import com.webApi.LibraryManagementSystem.model.LoanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanModel, Long> {
    Optional<LoanModel> findByBookIdAndReturnDateIsNull(Long book_id);


}
