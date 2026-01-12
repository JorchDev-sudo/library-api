package com.jorchdev.library_api.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    @DateTimeFormat
    private LocalDateTime publicationDate;

    @ManyToMany(mappedBy = "books")
    private List<Author> authors = new ArrayList<>();

    @Column(nullable = false)
    private Long stock = 0L;

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

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }
    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    public List<Author> getAuthors() {
        return authors;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
    public Long getStock() {
        return stock;
    }

    public Book(){}
}
