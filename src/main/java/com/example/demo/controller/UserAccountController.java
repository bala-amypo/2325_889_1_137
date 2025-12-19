package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.UserAccount;
import com.example.demo.service.UserAccountService;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping
    public UserAccount createUser(@RequestBody UserAccount userAccount) {
        return userAccountService.createUser(userAccount);
    }

    @GetMapping
    public List<UserAccount> getAllUsers() {
        return userAccountService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserAccount getUserById(@PathVariable Long id) {
        return userAccountService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserAccount updateUser(@PathVariable Long id, @RequestBody UserAccount userAccount) {
        return userAccountService.updateUser(id, userAccount);
    }

    @PutMapping("/{id}/deactivate")
    public UserAccount deactivateUser(@PathVariable Long id) {
        return userAccountService.deactivateUser(id);
    }
}
