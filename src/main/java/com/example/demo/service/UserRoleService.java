package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserRole;

@Service
public class UserRoleService {

    Map<Long, UserRole> mp = new HashMap<>();

    public UserRole assignRole(UserRole userRole) {
        mp.put(userRole.getId(), userRole);
        return userRole;
    }

    public List<UserRole> getRolesForUser(Long userId) {
        List<UserRole> list = new ArrayList<>();
        for (UserRole ur : mp.values()) {
            if (ur.getUser().getId().equals(userId)) {
                list.add(ur);
            }
        }
        return list;
    }

    public UserRole getMappingById(Long id) {
        return mp.get(id);
    }

    public void removeRole(Long id) {
        mp.remove(id);
    }
}
