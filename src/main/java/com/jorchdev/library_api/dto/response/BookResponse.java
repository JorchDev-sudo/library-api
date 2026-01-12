package com.jorchdev.library_api.dto.response;

import com.jorchdev.library_api.dto.summary.AuthorSummary;

import java.time.LocalDateTime;
import java.util.List;

public record BookResponse(
        String title,
        List<AuthorSummary> authors,
        String isbn,
        LocalDateTime publicationDate,
        long stock){ }
