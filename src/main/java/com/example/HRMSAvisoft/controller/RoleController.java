package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final ModelMapper modelMapper;

    private final RoleService roleService;

    RoleController(ModelMapper modelMapper, RoleService roleService){
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @PostMapping("/addRole")
    public ResponseEntity<Role> saveRole(@RequestBody String role) {
        Role roleAdded =roleService.addRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleAdded);
    }
}
