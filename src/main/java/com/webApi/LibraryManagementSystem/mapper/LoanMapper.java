package com.webApi.LibraryManagementSystem.mapper;

import com.webApi.LibraryManagementSystem.dto.LoanResponseDTO;
import com.webApi.LibraryManagementSystem.model.LoanModel;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    public LoanResponseDTO toLoanResponseDTO(LoanModel loan){
        LoanResponseDTO dto = new LoanResponseDTO();
        dto.setId(loan.getId());
        dto.setBookId(loan.getBook().getId());
        dto.setUserId(loan.getUser().getId());
        dto.setLoanDate(loan.getLoanDate());
        dto.setDueDate(loan.getLoanDate());
        dto.setReturnDate(loan.getReturnDate());
        return dto;
    }
}
