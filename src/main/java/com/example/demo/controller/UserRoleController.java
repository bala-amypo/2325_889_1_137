package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRoleRepository;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @PostMapping
    public UserRole assignRole(@RequestBody UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @GetMapping("/user/{userId}")
public List<UserRole> getRolesByUser(@PathVariable Long userId) {
    return userRoleRepository.findAll();
}


    @GetMapping("/{id}")
    public UserRole getMappingById(@PathVariable Long id) {
        return userRoleRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void removeRole(@PathVariable Long id) {
        userRoleRepository.deleteById(id);
    }
}







// package com.example.demo.controller;

// import java.util.List;

// import org.springframework.web.bind.annotation.*;

// import com.example.demo.entity.UserRole;
// import com.example.demo.service.UserRoleService;

// @RestController
// @RequestMapping("/api/user-roles")
// public class UserRoleController {

//     private final UserRoleService service;

//     public UserRoleController(UserRoleService service) {
//         this.service = service;
//     }

//     @PostMapping
//     public UserRole assignRole(@RequestBody UserRole mapping) {
//         return service.assignRole(mapping);
//     }

//     @GetMapping("/user/{userId}")
//     public List<UserRole> getRolesForUser(@PathVariable Long userId) {
//         return service.getRolesForUser(userId);
//     }

//     @GetMapping("/{id}")
//     public UserRole getMapping(@PathVariable Long id) {
//         return service.getMappingById(id);
//     }

//     @DeleteMapping("/{id}")
//     public void removeRole(@PathVariable Long id) {
//         service.removeRole(id);
//     }
// }
