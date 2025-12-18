package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.UserAccount;

public interface UserAccountService {

    UserAccount createUser(UserAccount user);

    UserAccount updateUser(Long id, UserAccount user);

    UserAccount getUserById(Long id);

    List<UserAccount> getAllUsers();

    UserAccount deactivateUser(Long id);
}
