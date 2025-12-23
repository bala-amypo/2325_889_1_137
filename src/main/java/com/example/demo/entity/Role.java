package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private String roleName;
    private String description;
    private boolean active = true;

    // getters & setters
}
