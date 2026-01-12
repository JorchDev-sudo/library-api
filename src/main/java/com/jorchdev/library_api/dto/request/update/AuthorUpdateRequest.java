package com.jorchdev.library_api.dto.request.update;

import java.util.List;

public class AuthorUpdateRequest {
    private String name;
    private List<Long> booksId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getBooksId() {
        return booksId;
    }
    public void setBooksId(List<Long> booksId) {
        this.booksId = booksId;
    }
}
