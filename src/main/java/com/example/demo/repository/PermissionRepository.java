// package com.example.demo.repository;

// import org.springframework.data.jpa.repository.JpaRepository;

// import org.springframework.stereotype.Repository;
// import com.example.demo.entity.Permission;

// @Repository

// public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
// }






package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionKey(String permissionKey);
}
