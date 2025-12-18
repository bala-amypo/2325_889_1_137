package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.UserRole;

public interface UserRoleService {

    UserRole assignRole(UserRole userRole);

    List<UserRole> getRolesForUser(Long userId);

    UserRole getMappingById(Long id);

    void removeRole(Long id);
}
