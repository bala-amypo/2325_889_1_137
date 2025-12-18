package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserAccount;

@Service
public class UserAccountService {

    Map<Long, UserAccount> mp = new HashMap<>();

    public UserAccount createUser(UserAccount user) {
        mp.put(user.getId(), user);
        return user;
    }

    public UserAccount updateUser(Long id, UserAccount user) {
        mp.replace(id, user);
        return user;
    }

    public UserAccount getUserById(Long id) {
        return mp.get(id);
    }

    public List<UserAccount> getAllUsers() {
        return new ArrayList<>(mp.values());
    }

    public UserAccount deactivateUser(Long id) {
        UserAccount user = mp.get(id);
        if (user != null) {
            user.setActive(false);
            mp.put(id, user);
        }
        return user;
    }
}
