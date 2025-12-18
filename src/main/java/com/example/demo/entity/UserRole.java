package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserRole {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private LocalDate assignedAt;

    
  
    public UserRole() {
    }
    public UserRole(Long id, LocalDate assignedAt) {
        this.id = id;
        this.assignedAt = assignedAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getAssignedAt() {
        return assignedAt;
    }
    public void setAssignedAt(LocalDate assignedAt) {
        this.assignedAt = assignedAt;
    }

}
