package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Permission;

public interface PermissionService {

    Permission createPermission(Permission permission);

    Permission updatePermission(Long id, Permission permission);

    Permission getPermissionById(Long id);

    List<Permission> getAllPermissions();

    Permission deactivatePermission(Long id);
}
