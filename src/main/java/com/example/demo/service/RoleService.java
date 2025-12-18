package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Role;

public interface RoleService {

    Role createRole(Role role);

    Role updateRole(Long id, Role role);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    Role deactivateRole(Long id);
}
