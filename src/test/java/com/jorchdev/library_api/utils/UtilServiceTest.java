package com.jorchdev.library_api.utils;

import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.models.Loan;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.AuthorRepository;
import com.jorchdev.library_api.repositories.BookRepository;
import com.jorchdev.library_api.repositories.LoanRepository;
import com.jorchdev.library_api.repositories.UserRepository;
import com.jorchdev.library_api.utils.UtilService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UtilServiceTest {
    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    LoanRepository loanRepository;

    @InjectMocks
    UtilService utilService;

    @Test
    public void shouldFindAuthor(){
        Author example = new Author();
        example.setName("example");
        example.setId(1L);

        when(authorRepository.findById(example.getId())).thenReturn(Optional.of(example));

        Author author = utilService.authorHelper(example.getId());

        assertThat(author).isEqualTo(example);

        verify(authorRepository).findById(example.getId());
    }

    @Test
    public void shouldFindBook(){
        Book example = new Book();
        example.setTitle("example");
        example.setId(1L);

        when(bookRepository.findById(example.getId())).thenReturn(Optional.of(example));

        Book book = utilService.bookHelper(example.getId());

        assertThat(book).isEqualTo(example);

        verify(bookRepository).findById(example.getId());
    }

    @Test
    public void shouldDecreaseStock(){
        Book example = new Book();
        example.setTitle("example");
        example.setId(1L);
        example.setStock(10L);

        UtilService.stockDecreaseHelper(example);

        assertThat(example.getStock()).isEqualTo(9);
    }

    @Test
    public void shouldIncreaseStock(){
        Book example = new Book();
        example.setTitle("example");
        example.setId(1L);
        example.setStock(9L);

        UtilService.stockIncreaseHelper(example);

        assertThat(example.getStock()).isEqualTo(10);


    }

    @Test
    public void shouldFindUser(){
        User example = new User();
        example.setId(1L);

        when(userRepository.findById(example.getId())).thenReturn(Optional.of(example));

        utilService.userHelper(example.getId());

        verify(userRepository).findById(example.getId());
    }

    @Test
    public void shouldFindLoan(){
        Loan example = new Loan();
        example.setId(1L);

        when(loanRepository.findById(example.getId())).thenReturn(Optional.of(example));

        utilService.loanHelper(example.getId());

        verify(loanRepository).findById(example.getId());
    }

    @Test
    void authorHelper_shouldThrowException_whenAuthorNotFound() {
        Long id = 1L;

        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilService.authorHelper(id))
                .isInstanceOf(EntityNotFoundException.class);

        verify(authorRepository).findById(id);
    }

    @Test
    void bookHelper_shouldThrowException_whenBookNotFound() {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilService.bookHelper(id))
                .isInstanceOf(RuntimeException.class);

        verify(bookRepository).findById(id);
    }

    @Test
    void userHelper_shouldThrowException_whenUserNotFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilService.userHelper(id))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).findById(id);
    }

    @Test
    void loanHelper_shouldThrowException_whenLoanNotFound() {
        Long id = 1L;

        when(loanRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilService.loanHelper(id))
                .isInstanceOf(EntityNotFoundException.class);

        verify(loanRepository).findById(id);
    }

    @Test
    void stockDecreaseHelper_shouldThrowException_whenStockIsZero() {
        Book book = new Book();
        book.setStock(0L);

        assertThatThrownBy(() -> UtilService.stockDecreaseHelper(book))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book is currently out of stock");
    }

    @Test
    void stockDecreaseHelper_shouldThrowException_whenStockIsNegative() {
        Book book = new Book();
        book.setStock(-1L);

        assertThatThrownBy(() -> UtilService.stockDecreaseHelper(book))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void stockIncreaseHelper_shouldThrowException_whenStockIsNegative() {
        Book book = new Book();
        book.setStock(-1L);

        assertThatThrownBy(() -> UtilService.stockIncreaseHelper(book))
                .isInstanceOf(IllegalStateException.class);
    }


}
