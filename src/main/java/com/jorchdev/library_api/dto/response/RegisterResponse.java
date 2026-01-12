package com.jorchdev.library_api.dto.response;

public record RegisterResponse(
        String message,
        String email,
        String name
){}
