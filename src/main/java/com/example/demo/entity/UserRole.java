// package com.example.demo.entity;

// import java.time.LocalDate;

// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Entity;

// @Entity
// public class UserRole {
//     @Id
//     @GeneratedValue(strategy=GenerationType.IDENTITY)
//     private Long id;
//     private LocalDate assignedAt;

    
  
//     public UserRole() {
//     }
//     public UserRole(Long id, LocalDate assignedAt) {
//         this.id = id;
//         this.assignedAt = assignedAt;
//     }
//     public Long getId() {
//         return id;
//     }
//     public void setId(Long id) {
//         this.id = id;
//     }
//     public LocalDate getAssignedAt() {
//         return assignedAt;
//     }
//     public void setAssignedAt(LocalDate assignedAt) {
//         this.assignedAt = assignedAt;
//     }

// }








package com.example.demo.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    private LocalDateTime assignedAt;

    public UserRole() {
    }

    public UserRole(Long id, UserAccount user, Role role, LocalDateTime assignedAt) {
        this.id = id;
        this.user = user;
        this.role = role;
        this.assignedAt = assignedAt;
    }

    @PrePersist
    public void onCreate() {
        this.assignedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public void prePersist() {
        this.assignedAt = LocalDateTime.now();
    }

    public void preUpdate() {
        // this.updatedAt = LocalDateTime.now();
    }

}
