package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    public Role addRole(Role role) throws RoleAlreadyExistsException, IllegalArgumentException{
        Role existingRole = roleRepository.findById(role.getRole()).orElse(null);

        if(existingRole != null){
            throw new RoleAlreadyExistsException(role.getRole() + " role already exists");
        }

        Role newRole = new Role();
        newRole.setRole(role.getRole());

        for (Privilege privilege : role.getPrivilege()) {
            if (Privilege.valueOf(privilege.name()) != null) {
                newRole.getPrivilege().add(privilege);
            } else {
                throw new IllegalArgumentException("Invalid privilege: " + privilege);
            }
        }

        return roleRepository.save(newRole);
    }

    public void updateRole(Role role) throws EntityNotFoundException, IllegalArgumentException{
        Role roleToUpdate = roleRepository.findById(role.getRole()).orElseThrow((()-> new EntityNotFoundException(role.getRole()+ " role not found")));

        roleToUpdate.getPrivilege().clear();

        for (Privilege privilege : role.getPrivilege()) {
            if (Privilege.valueOf(privilege.name()) != null) {
                roleToUpdate.getPrivilege().add(privilege);
            } else {
                throw new IllegalArgumentException("Invalid privilege: " + privilege);
            }
        }
        roleRepository.save(roleToUpdate);
    }

    public static class RoleAlreadyExistsException extends RuntimeException{
        public RoleAlreadyExistsException(String message) {
            super(message);
        }
    }
}
