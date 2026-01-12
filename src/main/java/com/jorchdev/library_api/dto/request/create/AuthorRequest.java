package com.jorchdev.library_api.dto.request.create;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class AuthorRequest {

    @NotBlank(message = "Name is required")
    String name;

    List<Long> booksId;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setBooksId(List<Long> booksId) {
        this.booksId = booksId;
    }
    public List<Long> getBooksId() {
        return booksId;
    }
}
