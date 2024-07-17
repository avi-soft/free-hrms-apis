package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.ChangeUserRoleDTO;
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
import java.util.Set;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    RoleService(RoleRepository roleRepository,  UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository=userRepository;
    }

    public List<Role> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    public Role addRole(Role role) throws RoleAlreadyExistsException, IllegalArgumentException{
        if(role.getRole().isEmpty() || role.getRole().equals("") || role.getRole().isEmpty()){
            throw new IllegalArgumentException("Role cannot be empty");
        }
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
    public Role deleteRole(Long roleId) throws EntityNotFoundException {
        Role roleToDelete = roleRepository.findById(roleId).orElseThrow((()-> new EntityNotFoundException(roleId+ "not found")));
        List<User> usersWithRole = userRepository.findByRoles(roleToDelete);
        for (User user : usersWithRole) {
            user.getRoles().remove(roleToDelete);
            userRepository.save(user);
        }
        roleToDelete.getPrivilege().clear();
        roleRepository.delete(roleToDelete);
        return roleToDelete;
    }

    public Role changeRoleOfUser(Long userId, Long oldRoleId,Long newRoleId) throws EntityNotFoundException,IllegalArgumentException, UserService.UserNotFoundException
    {
        User userWhoseRoleToChange= userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found"));
        Set<Role> roles = userWhoseRoleToChange.getRoles();
        Role oldRole = roleRepository.findById(oldRoleId).orElseThrow(() -> new EntityNotFoundException("Old role not found for user"));
        Role newRole = roleRepository.findById(newRoleId).orElseThrow(() -> new EntityNotFoundException("New role not found"));
            roles.remove(oldRole);
            roles.add(newRole);
            userRepository.save(userWhoseRoleToChange);
            return newRole;
    }

    public Role assignRoleToExistingUser(Long userId, Long roleId) throws EntityNotFoundException,IllegalArgumentException, UserService.UserNotFoundException
    {
        User userToWhomAssignRole= userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found"));
        Set<Role> roles = userToWhomAssignRole.getRoles();
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Old role not found for user"));
            roles.add(role);
            userRepository.save(userToWhomAssignRole);
            return role;
    }

    public static class RoleAlreadyExistsException extends RuntimeException{
        public RoleAlreadyExistsException(String message) {
            super(message);
        }
        }
}
