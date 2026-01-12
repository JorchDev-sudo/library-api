package com.jorchdev.library_api.repositories;

import com.jorchdev.library_api.models.Loan;
import com.jorchdev.library_api.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByUser(User user, Pageable pageable);
}
