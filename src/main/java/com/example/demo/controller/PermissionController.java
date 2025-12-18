package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Permission;
import com.example.demo.repository.PermissionRepository;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionRepository.save(permission);
    }

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Permission updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        Permission existing = permissionRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setPermissionKey(permission.getPermissionKey());
            existing.setDescription(permission.getDescription());
            existing.setActive(permission.getActive());
            return permissionRepository.save(existing);
        }
        return null;
    }

    @PutMapping("/{id}/deactivate")
    public Permission deactivatePermission(@PathVariable Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            permission.setActive(false);
            return permissionRepository.save(permission);
        }
        return null;
    }
}
