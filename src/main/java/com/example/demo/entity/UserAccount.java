package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class UserAccount {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String fullName;
    private boolean active = true;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    // getters & setters
}
