package com.jorchdev.library_api.dto.request.create;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LoanRequest {
    @NotNull(message = "Book ID is required")
    private Long bookId;

    private LocalDateTime dueDate;

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
