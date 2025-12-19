package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @PostMapping
    public UserAccount createUser(@RequestBody UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    @GetMapping
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserAccount getUserById(@PathVariable Long id) {
        return userAccountRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public UserAccount updateUser(@PathVariable Long id, @RequestBody UserAccount userAccount) {
        return userAccountRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(userAccount.getUsername());
            existingUser.setEmail(userAccount.getEmail());
            existingUser.setActive(userAccount.getActive());
            return userAccountRepository.save(existingUser);
        }).orElse(null);
    }

    @PutMapping("/{id}/deactivate")
    public UserAccount deactivateUser(@PathVariable Long id) {
        return userAccountRepository.findById(id).map(existingUser -> {
            existingUser.setActive(false);
            return userAccountRepository.save(existingUser);
        }).orElse(null);
    }
}
