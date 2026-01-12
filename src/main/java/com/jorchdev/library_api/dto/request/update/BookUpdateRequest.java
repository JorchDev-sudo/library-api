package com.jorchdev.library_api.dto.request.update;

import java.time.LocalDateTime;
import java.util.List;

public class BookUpdateRequest {
    private String title;
    private String isbn;
    private LocalDateTime publicationDate;
    private Long stock;
    private List<Long> authorsId;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    public List<Long> getAuthorsId() {
        return authorsId;
    }
    public void setAuthorsId(List<Long> authorsId) {
        this.authorsId = authorsId;
    }

    public Long getStock() {
        return stock;
    }
    public void setStock(Long stock) {
        this.stock = stock;
    }
}
