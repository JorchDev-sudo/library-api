package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.create.LoanRequest;
import com.jorchdev.library_api.dto.response.LoanResponse;
import com.jorchdev.library_api.mappers.LoanMapper;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.models.Loan;
import com.jorchdev.library_api.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LoanMapperTest {

    private final LoanMapper loanMapper = new LoanMapper();

    @Test
    void toEntity_shouldCreateLoanWithDefaultDueDateWhenRequestDueDateIsNull() {
        LoanRequest request = new LoanRequest();
        request.setBookId(1L);

        User user = new User();
        Book book = new Book();

        LocalDateTime beforeCreation = LocalDateTime.now();

        Loan loan = loanMapper.toEntity(request, user, book);

        LocalDateTime afterCreation = LocalDateTime.now();

        assertThat(loan.getUser()).isEqualTo(user);
        assertThat(loan.getBook()).isEqualTo(book);

        assertThat(loan.getDueDate())
                .isAfterOrEqualTo(beforeCreation.plusDays(7))
                .isBeforeOrEqualTo(afterCreation.plusDays(7));
    }

    @Test
    void toEntity_shouldAddSevenDaysToProvidedDueDate() {
        LoanRequest request = new LoanRequest();
        LocalDateTime requestedDueDate = LocalDateTime.of(2026, 1, 1, 10, 0);
        request.setDueDate(requestedDueDate);

        User user = new User();
        Book book = new Book();

        Loan loan = loanMapper.toEntity(request, user, book);

        assertThat(loan.getDueDate())
                .isEqualTo(requestedDueDate.plusDays(7));
    }

    @Test
    void toResponse_shouldMapLoanToLoanResponse() {
        User user = new User();
        user.setEmail("jorgecoteralopez@gmail.com");

        Book book = new Book();
        book.setTitle("Metamorphosis");

        LocalDateTime loanDate = LocalDateTime.of(2026, 1, 1, 12, 0);
        LocalDateTime dueDate = loanDate.plusDays(7);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(loanDate);
        loan.setDueDate(dueDate);

        LoanResponse response = loanMapper.toResponse(loan);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.bookTitle()).isEqualTo("Metamorphosis");
        assertThat(response.userEmail()).isEqualTo("jorgecoteralopez@gmail.com");
        assertThat(response.loanDate()).isEqualTo(loanDate);
        assertThat(response.dueDate()).isEqualTo(dueDate);
    }
}
