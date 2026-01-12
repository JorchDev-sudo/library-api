package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.create.LoanRequest;
import com.jorchdev.library_api.dto.response.LoanResponse;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.models.Loan;
import com.jorchdev.library_api.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanMapper {
    public Loan toEntity (LoanRequest loanRequest, User user, Book book){
        Loan newLoan = new Loan();
        newLoan.setUser(user);
        newLoan.setBook(book);

        if (loanRequest.getDueDate() == null){
            newLoan.setDueDate(LocalDateTime.now().plusDays(7));

        } else {
            newLoan.setDueDate(loanRequest.getDueDate().plusDays(7));
        }

        return newLoan;
    }

    public LoanResponse toResponse (Loan loan){
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getTitle(),
                loan.getUser().getEmail(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate());
    }

}
