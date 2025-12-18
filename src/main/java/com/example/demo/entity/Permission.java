package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String permissionKey;
    private String description;
    private Boolean active;
    
    
   
    public Permission(Long id, String permissionKey, String description, Boolean active) {
        this.id = id;
        this.permissionKey = permissionKey;
        this.description = description;
        this.active = active;
    }
    public Permission() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPermissionKey() {
        return permissionKey;
    }
    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

}
