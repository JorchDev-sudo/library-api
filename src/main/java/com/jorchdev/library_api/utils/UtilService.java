package com.jorchdev.library_api.utils;

import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.models.Loan;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.AuthorRepository;
import com.jorchdev.library_api.repositories.BookRepository;
import com.jorchdev.library_api.repositories.LoanRepository;
import com.jorchdev.library_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    public UtilService (
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            UserRepository userRepository,
            LoanRepository loanRepository)
    {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    public Author authorHelper (Long id){
        return authorRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Book bookHelper(Long id){
        return bookRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public static Book stockDecreaseHelper(Book book){
        if (book.getStock() <= 0){
            throw new RuntimeException("Book is currently out of stock");
        }

        book.setStock(book.getStock() - 1);

        return book;
    }

    public static Book stockIncreaseHelper(Book book){
        if (book.getStock() < 0){
            throw new IllegalStateException();
        }

        book.setStock(book.getStock() + 1);

        return book;
    }

    public User userHelper (long id){
        return userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Loan loanHelper (Long id){
        return loanRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
