package com.jorchdev.library_api.dto.response;

import com.jorchdev.library_api.dto.summary.BookSummary;

import java.util.List;

public record AuthorResponse(
        Long id,
        String name,
        List<BookSummary> bookSummaries) {}
