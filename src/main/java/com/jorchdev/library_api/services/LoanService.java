package com.jorchdev.library_api.services;

import com.jorchdev.library_api.dto.request.create.LoanRequest;
import com.jorchdev.library_api.dto.response.LoanResponse;
import com.jorchdev.library_api.mappers.LoanMapper;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.models.Loan;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.BookRepository;
import com.jorchdev.library_api.repositories.LoanRepository;
import com.jorchdev.library_api.utils.UtilService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final BookRepository bookRepository;
    private final UtilService utilService;

    @Autowired
    public LoanService(
            LoanRepository loanRepository,
            LoanMapper loanMapper,
            BookRepository bookRepository,
            UtilService utilService)

    {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.bookRepository = bookRepository;
        this.utilService = utilService;
    }

    @Transactional
    public LoanResponse createLoan(LoanRequest request, Long id) {
        Book book = utilService.bookHelper(request.getBookId());
        User user = utilService.userHelper(id);

        if (book.getStock() <= 0) {
            throw new IllegalStateException("Book is currently out of stock");
        }

        Loan newLoan = loanMapper.toEntity(request, user, book);
        Loan savedLoan = loanRepository.save(newLoan);

        bookRepository.save(UtilService.stockDecreaseHelper(book));

        return loanMapper.toResponse(savedLoan);
    }

    @Transactional
    public void returnBook(Long loanId) {
        Loan loan = utilService.loanHelper(loanId);

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("This book has already been returned");
        }

        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan);

        Book book = utilService.bookHelper(loan.getBook().getId());
        bookRepository.save(UtilService.stockIncreaseHelper(book));
    }

    public List<LoanResponse> findLoans (
            long userId,
            int page,
            int size,
            String direction)
    {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("loanDate").ascending()
                : Sort.by("loanDate").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Loan> loanPage;

        loanPage = loanRepository.findByUser(utilService.userHelper(userId), pageable);

        return loanPage
                .stream()
                .map(loanMapper::toResponse).toList();
    }
}