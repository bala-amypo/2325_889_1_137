// package com.example.demo.entity;

// import java.time.LocalDate;

// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Entity;

// @Entity
// public class RolePermission {
//     @Id
//     @GeneratedValue(strategy=GenerationType.IDENTITY)
//     private Long id;
//     private LocalDate grantedAt;
    
    
//     public RolePermission() {
//     }
//     public RolePermission(Long id, LocalDate grantedAt) {
//         this.id = id;
//         this.grantedAt = grantedAt;
//     }
//     public Long getId() {
//         return id;
//     }
//     public void setId(Long id) {
//         this.id = id;
//     }
//     public LocalDate getGrantedAt() {
//         return grantedAt;
//     }
//     public void setGrantedAt(LocalDate grantedAt) {
//         this.grantedAt = grantedAt;
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
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    private LocalDateTime grantedAt;

   
    public RolePermission() {
    }

    
    public RolePermission(Long id, Role role, Permission permission, LocalDateTime grantedAt) {
        this.id = id;
        this.role = role;
        this.permission = permission;
        this.grantedAt = grantedAt;
    }

    @PrePersist
    public void onCreate() {
        this.grantedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
        this.grantedAt = grantedAt;
    }

    public void prePersist() {
        this.grantedAt = LocalDateTime.now();
    }

    public void preUpdate() {
        // this.updatedAt = LocalDateTime.now();
    }

}
