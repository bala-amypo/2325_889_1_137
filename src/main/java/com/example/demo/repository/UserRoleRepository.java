// package com.example.demo.repository;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.demo.entity.UserRole;

// import org.springframework.stereotype.Repository;
// @Repository
// public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
// }









package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser_Id(Long userId);
}
