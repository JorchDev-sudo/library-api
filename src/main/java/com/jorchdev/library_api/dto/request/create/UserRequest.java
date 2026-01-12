package com.jorchdev.library_api.dto.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "Name is mandatory")
    public String name;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
