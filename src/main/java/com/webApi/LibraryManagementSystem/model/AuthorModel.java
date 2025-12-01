package com.webApi.LibraryManagementSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class AuthorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //Getters y Setters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public AuthorModel(String name){
        this.name=name;
    }
}
