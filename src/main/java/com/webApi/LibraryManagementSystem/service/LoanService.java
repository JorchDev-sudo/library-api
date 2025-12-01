package com.webApi.LibraryManagementSystem.service;

import com.webApi.LibraryManagementSystem.dto.LoanRequestDTO;
import com.webApi.LibraryManagementSystem.dto.LoanResponseDTO;
import com.webApi.LibraryManagementSystem.mapper.LoanMapper;
import com.webApi.LibraryManagementSystem.model.BookModel;
import com.webApi.LibraryManagementSystem.model.LoanModel;
import com.webApi.LibraryManagementSystem.model.UserModel;
import com.webApi.LibraryManagementSystem.repository.BookRepository;
import com.webApi.LibraryManagementSystem.repository.LoanRepository;
import com.webApi.LibraryManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.loanMapper = loanMapper;
    }

    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO requestDTO) {
        BookModel book = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        UserModel user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (book.getStock() <= 0) {
            throw new IllegalStateException("Book is currently out of stock");
        }

        LoanModel loan = new LoanModel();
        loan.setBook(book);
        loan.setUser(user);

        LocalDateTime dueDate = LocalDateTime.now();
        loan.setDueDate(dueDate.plusDays(7));

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        LoanModel savedLoan = loanRepository.save(loan);

        return loanMapper.toLoanResponseDTO(savedLoan);
    }

    @Transactional
    public LoanResponseDTO returnBook(Long loanId) {
        LoanModel loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("This book has already been returned");
        }

        loan.setReturnDate(LocalDateTime.now());
        LoanModel returnedLoan = loanRepository.save(loan);

        BookModel book = returnedLoan.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        return loanMapper.toLoanResponseDTO(returnedLoan);
    }

    public LoanResponseDTO findById(Long loanId) {
        LoanModel loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        return loanMapper.toLoanResponseDTO(loan);
    }

    public List<LoanResponseDTO> findAll() {
    List<LoanModel> loans = loanRepository.findAll();

    return loans.stream()
            .map(loanMapper::toLoanResponseDTO)
            .collect(Collectors.toList());

    }
}