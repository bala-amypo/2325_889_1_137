package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.RolePermission;
import com.example.demo.repository.RolePermissionRepository;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @PostMapping
    public RolePermission grantPermission(@RequestBody RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    @GetMapping
    public List<RolePermission> getAllRolePermissions() {
        return rolePermissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public RolePermission getById(@PathVariable Long id) {
        return rolePermissionRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void revokePermission(@PathVariable Long id) {
        rolePermissionRepository.deleteById(id);
    }
}








// package com.example.demo.controller;

// import java.util.List;

// import org.springframework.web.bind.annotation.*;

// import com.example.demo.entity.RolePermission;
// import com.example.demo.service.RolePermissionService;

// @RestController
// @RequestMapping("/api/role-permissions")
// public class RolePermissionController {

//     private final RolePermissionService service;

//     public RolePermissionController(RolePermissionService service) {
//         this.service = service;
//     }

//     @PostMapping
//     public RolePermission grantPermission(@RequestBody RolePermission mapping) {
//         return service.grantPermission(mapping);
//     }

//     @GetMapping("/role/{roleId}")
//     public List<RolePermission> getPermissionsForRole(@PathVariable Long roleId) {
//         return service.getPermissionsForRole(roleId);
//     }

//     @GetMapping("/{id}")
//     public RolePermission getMapping(@PathVariable Long id) {
//         return service.getMappingById(id);
//     }

//     @DeleteMapping("/{id}")
//     public void revokePermission(@PathVariable Long id) {
//         service.revokePermission(id);
//     }
// }
