package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.UserAccount;

public interface UserAccountService {

    UserAccount createUser(UserAccount user);

    UserAccount updateUser(long id, UserAccount user);

    UserAccount getUserById(long id);

    void deactivateUser(long id);

    List<UserAccount> getAllUsers();
}
