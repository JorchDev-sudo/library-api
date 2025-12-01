package com.webApi.LibraryManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequestDTO {


    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "MemberID is mandatory")
    private String memberId;

    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
