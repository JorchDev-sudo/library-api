package com.jorchdev.library_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    public String email;

    @NotBlank(message = "Password is required")
    public String password;

    public LoginRequest(){}

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
}
