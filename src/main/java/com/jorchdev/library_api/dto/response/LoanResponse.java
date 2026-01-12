package com.jorchdev.library_api.dto.response;

import java.time.LocalDateTime;

public record LoanResponse(
        Long id,
        String bookTitle,
        String userEmail,
        LocalDateTime loanDate,
        LocalDateTime dueDate,
        LocalDateTime returnDate) { }
