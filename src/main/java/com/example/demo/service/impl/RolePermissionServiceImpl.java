// package com.example.demo.service.impl;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.stereotype.Service;

// import com.example.demo.entity.RolePermission;
// import com.example.demo.service.RolePermissionService;

// @Service
// public class RolePermissionServiceImpl implements RolePermissionService {

//     Map<Long, RolePermission> mp = new HashMap<>();

//     public RolePermission grantPermission(RolePermission rolePermission) {
//         mp.put(rolePermission.getId(), rolePermission);
//         return rolePermission;
//     }

//     public List<RolePermission> getPermissionsForRole(Long roleId) {
//         return new ArrayList<>(mp.values());
//     }

//     public RolePermission getMappingById(Long id) {
//         return mp.get(id);
//     }

//     public void revokePermission(Long id) {
//         mp.remove(id);
//     }
// }





package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.RolePermission;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

   
    public RolePermissionServiceImpl(
            RolePermissionRepository rolePermissionRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RolePermission grantPermission(RolePermission mapping) {

        Role role = roleRepository.findById(mapping.getRole().getId())
                .orElseThrow(() -> new BadRequestException("Role not found"));

        if (!Boolean.TRUE.equals(role.getActive())) {
            throw new BadRequestException("Role is inactive");
        }

        Permission permission = permissionRepository.findById(mapping.getPermission().getId())
                .orElseThrow(() -> new BadRequestException("Permission not found"));

        if (!Boolean.TRUE.equals(permission.getActive())) {
            throw new BadRequestException("Permission is inactive");
        }

        mapping.setRole(role);
        mapping.setPermission(permission);

        return rolePermissionRepository.save(mapping);
    }

    @Override
    public List<RolePermission> getPermissionsForRole(Long roleId) {
        return rolePermissionRepository.findByRole_Id(roleId);
    }

    @Override
    public RolePermission getMappingById(Long id) {
        return rolePermissionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("RolePermission mapping not found"));
    }

    @Override
    public void revokePermission(Long mappingId) {
        RolePermission mapping = getMappingById(mappingId);
        rolePermissionRepository.deleteById(mapping.getId());
    }
}
