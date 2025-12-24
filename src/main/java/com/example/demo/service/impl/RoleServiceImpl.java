package com.example.demo.service.impl;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Role;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            throw new BadRequestException("Role already exists");
        }
        role.setActive(true);
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(long id, Role role) {
        Role existing = getRoleById(id);
        existing.setRoleName(role.getRoleName());
        existing.setDescription(role.getDescription());
        return roleRepository.save(existing);
    }

    @Override
    public void deactivateRole(long id) {
        Role role = getRoleById(id);
        role.setActive(false);
        roleRepository.save(role);
    }
}
