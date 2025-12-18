package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;

@Service
public class RoleService {

    Map<Long, Role> mp = new HashMap<>();

    public Role createRole(Role role) {
        mp.put(role.getId(), role);
        return role;
    }

    public Role updateRole(Long id, Role role) {
        mp.replace(id, role);
        return role;
    }

    public Role getRoleById(Long id) {
        return mp.get(id);
    }

    public List<Role> getAllRoles() {
        return new ArrayList<>(mp.values());
    }

    public Role deactivateRole(Long id) {
        Role role = mp.get(id);
        if (role != null) {
            role.setActive(false);
            mp.put(id, role);
        }
        return role;
    }
}
