package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.RolePermission;

public interface RolePermissionService {

    RolePermission grantPermission(RolePermission rolePermission);

    List<RolePermission> getPermissionsForRole(Long roleId);

    RolePermission getMappingById(Long id);

    void revokePermission(Long id);
}
