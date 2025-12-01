package com.webApi.LibraryManagementSystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BookRequestDTO {
    @NotBlank(message = "title is mandatory")
    private String title;

    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    private Integer publicationYear;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Author IDs list is mandatory")
    private List<Long> authorsIds;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
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

    public Integer getPublicationYear() {
        return publicationYear;
    }
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
}
