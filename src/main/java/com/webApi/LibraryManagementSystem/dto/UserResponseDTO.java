package com.webApi.LibraryManagementSystem.dto;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String memberId;

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public String getMemberId() {
        return memberId;
    }
}
