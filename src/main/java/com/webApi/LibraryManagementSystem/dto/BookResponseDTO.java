package com.webApi.LibraryManagementSystem.dto;

import java.util.Set;

public class BookResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Integer stock;

    private Set<AuthorResponseDTO> authors;

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Integer getStock() {
        return stock;
    }

    public void setAuthors(Set<AuthorResponseDTO> authors) {
        this.authors = authors;
    }
    public Set<AuthorResponseDTO> getAuthors() {
        return authors;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    public Integer getPublicationYear() {
        return publicationYear;
    }
}
