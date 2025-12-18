package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.RolePermission;
import com.example.demo.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    Map<Long, RolePermission> mp = new HashMap<>();

    public RolePermission grantPermission(RolePermission rolePermission) {
        mp.put(rolePermission.getId(), rolePermission);
        return rolePermission;
    }

    public List<RolePermission> getPermissionsForRole(Long roleId) {
        List<RolePermission> list = new ArrayList<>();
        for (RolePermission rp : mp.values()) {
            if (rp.getRole().getId().equals(roleId)) {
                list.add(rp);
            }
        }
        return list;
    }

    public RolePermission getMappingById(Long id) {
        return mp.get(id);
    }

    public void revokePermission(Long id) {
        mp.remove(id);
    }
}
