package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRoleRepository;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @PostMapping
    public UserRole assignRole(@RequestBody UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @GetMapping("/user/{userId}")
    public List<UserRole> getRolesByUser(@PathVariable Long userId) {
        return userRoleRepository.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public UserRole getMappingById(@PathVariable Long id) {
        return userRoleRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void removeRole(@PathVariable Long id) {
        userRoleRepository.deleteById(id);
    }
}
