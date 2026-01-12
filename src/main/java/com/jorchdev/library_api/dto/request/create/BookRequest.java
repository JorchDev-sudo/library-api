package com.jorchdev.library_api.dto.request.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class BookRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Publication date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publicationDate;

    @Min(value = 0, message = "Stock cannot be negative")
    private Long stock;

    @NotNull(message = "Author IDs list is required")
    private List<Long> authorsIds;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Long getStock() {
        return stock;
    }
    public void setStock(Long stock) {
        this.stock = stock;
    }

    public List<Long> getAuthorsIds() {
        return authorsIds;
    }
    public void setAuthorsIds(List<Long> authorsIds) {
        this.authorsIds = authorsIds;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public BookRequest(){}
}
