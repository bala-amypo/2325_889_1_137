package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Permission;
import com.example.demo.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    Map<Long, Permission> mp = new HashMap<>();

    public Permission createPermission(Permission permission) {
        mp.put(permission.getId(), permission);
        return permission;
    }

    public Permission updatePermission(Long id, Permission permission) {
        mp.replace(id, permission);
        return permission;
    }

    public Permission getPermissionById(Long id) {
        return mp.get(id);
    }

    public List<Permission> getAllPermissions() {
        return new ArrayList<>(mp.values());
    }

    public Permission deactivatePermission(Long id) {
        Permission permission = mp.get(id);
        if (permission != null) {
            permission.setActive(false);
            mp.put(id, permission);
        }
        return permission;
    }
}
