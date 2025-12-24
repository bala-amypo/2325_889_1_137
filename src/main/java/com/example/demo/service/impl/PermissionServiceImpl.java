package com.example.demo.service.impl;

import com.example.demo.entity.Permission;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.service.PermissionService;

import java.util.List;

public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission createPermission(Permission permission) {
        if (permissionRepository.findByPermissionKey(permission.getPermissionKey()).isPresent()) {
            throw new BadRequestException("Permission already exists");
        }
        permission.setActive(true);
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Long id, Permission updated) {
        Permission p = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        p.setPermissionKey(updated.getPermissionKey());
        return permissionRepository.save(p);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }

    @Override
    public void deactivatePermission(Long id) {
        Permission p = getPermissionById(id);
        p.setActive(false);
        permissionRepository.save(p);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
