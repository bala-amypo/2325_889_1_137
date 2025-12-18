package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserRole;
import com.example.demo.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    Map<Long, UserRole> mp = new HashMap<>();

    public UserRole assignRole(UserRole userRole) {
        mp.put(userRole.getId(), userRole);
        return userRole;
    }

    public List<UserRole> getRolesForUser(Long userId) {
        return new ArrayList<>(mp.values());
    }

    public UserRole getMappingById(Long id) {
        return mp.get(id);
    }

    public void removeRole(Long id) {
        mp.remove(id);
    }
}
