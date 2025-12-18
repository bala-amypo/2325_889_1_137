package com.example.demo.entity;

import java.time.LocalDate;

public class UserAccount {
    private Long id;
    private String email;
    private String fullName;
    private Boolean active;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    
    public UserAccount() {
    }
    public UserAccount(Long id, String email, String fullName, Boolean active, LocalDate createdAt,
            LocalDate updatedAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    } 
}
