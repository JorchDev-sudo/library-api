package com.jorchdev.library_api.service;

import com.jorchdev.library_api.dto.request.create.LoanRequest;
import com.jorchdev.library_api.dto.response.LoanResponse;
import com.jorchdev.library_api.mappers.LoanMapper;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.models.Loan;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.BookRepository;
import com.jorchdev.library_api.repositories.LoanRepository;
import com.jorchdev.library_api.services.LoanService;
import com.jorchdev.library_api.utils.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    @Mock
    LoanRepository loanRepository;

    @Mock
    LoanMapper loanMapper;

    @Mock
    BookRepository bookRepository;

    @Mock
    UtilService utilService;

    @InjectMocks
    LoanService loanService;

    @Test
    void createLoan_shouldCreateLoan_whenStockAvailable() {
        LoanRequest request = new LoanRequest();
        request.setBookId(1L);

        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");

        Book book = new Book();
        book.setId(1L);
        book.setStock(5L);

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);

        LoanResponse response = new LoanResponse(
                1L,
                "Metamorphosis",
                user.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                null
        );

        when(utilService.bookHelper(1L)).thenReturn(book);
        when(utilService.userHelper(1L)).thenReturn(user);
        when(loanMapper.toEntity(request, user, book)).thenReturn(loan);
        when(loanRepository.save(loan)).thenReturn(loan);
        when(loanMapper.toResponse(loan)).thenReturn(response);

        LoanResponse result = loanService.createLoan(request, 1L);

        assertThat(result).isEqualTo(response);

        verify(utilService).bookHelper(1L);
        verify(utilService).userHelper(1L);
        verify(loanMapper).toEntity(request, user, book);
        verify(loanRepository).save(loan);
        verify(bookRepository).save(any(Book.class));
        verify(loanMapper).toResponse(loan);
    }


    @Test
    void returnBook_shouldReturnLoan_whenNotReturned() {
        Book book = new Book();
        book.setId(1L);
        book.setStock(5L);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setReturnDate(null);

        when(utilService.loanHelper(1L)).thenReturn(loan);
        when(utilService.bookHelper(1L)).thenReturn(book);

        loanService.returnBook(1L);

        verify(loanRepository).save(loan);
        verify(bookRepository).save(any(Book.class));
    }


    @Test
    void findLoans_shouldReturnLoans() {
        User user = new User();
        user.setId(1L);

        Loan loan = new Loan();

        Page<Loan> page = new PageImpl<>(List.of(loan));

        when(utilService.userHelper(1L)).thenReturn(user);
        when(loanRepository.findByUser(eq(user), any(PageRequest.class)))
                .thenReturn(page);
        when(loanMapper.toResponse(loan)).thenReturn(mock(LoanResponse.class));

        List<LoanResponse> result = loanService.findLoans(1L, 0, 10, "desc");

        assertThat(result).hasSize(1);

        verify(loanRepository).findByUser(eq(user), any(PageRequest.class));
    }


    @Test
    void createLoan_shouldThrowException_whenBookOutOfStock() {
        LoanRequest request = new LoanRequest();
        request.setBookId(1L);

        Book book = new Book();
        book.setStock(0L);

        when(utilService.bookHelper(1L)).thenReturn(book);
        when(utilService.userHelper(1L)).thenReturn(new User());

        assertThatThrownBy(() -> loanService.createLoan(request, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Book is currently out of stock");

        verify(utilService).bookHelper(1L);
    }

    @Test
    void returnBook_shouldThrowException_whenAlreadyReturned() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setReturnDate(LocalDateTime.now());

        when(utilService.loanHelper(1L)).thenReturn(loan);

        assertThatThrownBy(() -> loanService.returnBook(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This book has already been returned");

        verify(utilService).loanHelper(1L);
    }

}
