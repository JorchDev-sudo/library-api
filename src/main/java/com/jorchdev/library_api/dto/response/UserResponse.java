package com.jorchdev.library_api.dto.response;

import com.jorchdev.library_api.models.Loan;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String email,
        List<Loan> loans) { }
