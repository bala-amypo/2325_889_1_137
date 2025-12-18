package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

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
            existing.setRoleKey(role.getRoleKey());
            existing.setDescription(role.getDescription());
            existing.setActive(role.getActive());
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
