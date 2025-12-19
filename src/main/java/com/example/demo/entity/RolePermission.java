package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class RolePermission {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private LocalDate grantedAt;
    
    
    public RolePermission() {
    }
    public RolePermission(Long id, LocalDate grantedAt) {
        this.id = id;
        this.grantedAt = grantedAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getGrantedAt() {
        return grantedAt;
    }
    public void setGrantedAt(LocalDate grantedAt) {
        this.grantedAt = grantedAt;
    }
}
