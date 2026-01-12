package com.jorchdev.library_api.models;

import com.jorchdev.library_api.models.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }
    public void setRole(Roles role) {
        this.role = role;
    }

    public List<Loan> getLoans() {
        return loans;
    }
    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public User(){}
}
