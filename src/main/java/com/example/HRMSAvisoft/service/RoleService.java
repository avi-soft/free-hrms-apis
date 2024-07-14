package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.repository.RoleRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    public Role addRole(Role role) throws RoleAlreadyExistsException, IllegalArgumentException{
        Role existingRole = roleRepository.getByRole(role.getRole()).orElse(null);

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

    public void updateRole(Role role, Long roleId) throws EntityNotFoundException, IllegalArgumentException{
        Role roleToUpdate = roleRepository.findById(roleId).orElseThrow((()-> new EntityNotFoundException("Role not found")));

        if (role.getRole() != null && !role.getRole().isEmpty() && !role.getRole().equals(roleToUpdate.getRole())) {
            Role newRole = roleRepository.getByRole(role.getRole()).orElse(null);
            if(newRole != null){
                throw new RoleAlreadyExistsException("Role by name " + role.getRole()+ " already exists.");
            }
            else{
                roleToUpdate.setRole(role.getRole());
            }
        }

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

    @Transactional
    public Role deleteRole(Role role) throws EntityNotFoundException {
        Role roleToDelete = roleRepository.getByRole(role.getRole()).orElseThrow((()-> new EntityNotFoundException(role.getRole()+ " role not found")));
        List<User> usersWithRole = userRepository.findByRoles(roleToDelete);
        for (User user : usersWithRole) {
            user.getRoles().remove(roleToDelete);
            userRepository.save(user);
        }
        roleToDelete.getPrivilege().clear();
        roleRepository.delete(roleToDelete);
        return roleToDelete;
    }

    public static class RoleAlreadyExistsException extends RuntimeException{
        public RoleAlreadyExistsException(String message) {
            super(message);
        }
        }
}
