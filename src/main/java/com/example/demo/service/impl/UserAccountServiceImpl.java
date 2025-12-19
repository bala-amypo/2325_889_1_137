package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserAccount createUser(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    @Override
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    @Override
    public UserAccount getUserById(Long id) {
        return userAccountRepository.findById(id).orElse(null);
    }

    @Override
    public UserAccount updateUser(Long id, UserAccount userAccount) {
        return userAccountRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(userAccount.getUsername());
            existingUser.setEmail(userAccount.getEmail());
            existingUser.setActive(userAccount.getActive());
            return userAccountRepository.save(existingUser);
        }).orElse(null);
    }

    @Override
    public UserAccount deactivateUser(Long id) {
        return userAccountRepository.findById(id).map(existingUser -> {
            existingUser.setActive(false);
            return userAccountRepository.save(existingUser);
        }).orElse(null);
    }
}
