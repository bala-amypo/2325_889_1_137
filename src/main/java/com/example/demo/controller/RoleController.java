package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleRepository.findById(id).orElse(null);
    }

   @PutMapping("/{id}")
public Role updateRole(@PathVariable Long id, @RequestBody Role role) {
    Role existing = roleRepository.findById(id).orElse(null);
    if (existing != null) {
        existing.setRoleName(role.getRoleName());
        return roleRepository.save(existing);
    }
    return null;
}

    @PutMapping("/{id}/deactivate")
    public Role deactivateRole(@PathVariable Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            role.setActive(false);
            return roleRepository.save(role);
        }
        return null;
    }
}





// package com.example.demo.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import com.example.demo.entity.Role;
// import com.example.demo.service.RoleService;

// @RestController
// @RequestMapping("/api/roles")
// public class RoleController {

//     @Autowired
//     private RoleService service;

//     @PostMapping
//     public Role createRole(@RequestBody Role role) {
//         return service.createRole(role);
//     }

//     @GetMapping
//     public List<Role> getAllRoles() {
//         return service.getAllRoles();
//     }

//     @GetMapping("/{id}")
//     public Role getRoleById(@PathVariable long id) {
//         return service.getRoleById(id);
//     }

//     @PutMapping("/{id}")
//     public Role updateRole(@PathVariable long id, @RequestBody Role role) {
//         return service.updateRole(id, role);
//     }

//     @PutMapping("/{id}/deactivate")
//     public void deactivateRole(@PathVariable long id) {
//         service.deactivateRole(id);
//     }
// }
