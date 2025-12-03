package com.webApi.LibraryManagementSystem.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class BookModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String isbn;
    @Column(nullable = false)
    private Integer publicationYear;

    @ManyToMany(mappedBy = "books")
    private Set<AuthorModel> authors = new HashSet<>();

    @Column(nullable = false)
    private Integer stock = 0;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<AuthorModel> getAuthors() {
        return authors;
    }
    public void setAuthors(Set<AuthorModel> authors) {
        this.authors = authors;
    }

    public BookModel(){}
    public BookModel(String title, String isbn, Integer publicationYear, Integer stock){
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.stock = stock;
    }
}
