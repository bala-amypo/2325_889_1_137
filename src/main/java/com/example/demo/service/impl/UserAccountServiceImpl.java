package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserAccount;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccount createUser(UserAccount user) {
        if (userAccountRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        user.setActive(true);
        return userAccountRepository.save(user);
    }

    @Override
    public UserAccount updateUser(long id, UserAccount user) {
        UserAccount existing = getUserById(id);
        existing.setEmail(user.getEmail());
        existing.setFullName(user.getFullName());
        return userAccountRepository.save(existing);
    }

    @Override
    public UserAccount getUserById(long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deactivateUser(long id) {
        UserAccount user = getUserById(id);
        user.setActive(false);
        userAccountRepository.save(user);
    }

    @Override
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }
}
