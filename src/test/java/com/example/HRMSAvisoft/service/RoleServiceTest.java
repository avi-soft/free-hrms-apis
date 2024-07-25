package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.controller.RoleJsonReader;
import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.repository.RoleRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    RoleService roleService;
    RoleRepository roleRepository;
    UserRepository userRepository;

    Role role;
    RoleJsonReader jsonReader = new RoleJsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Role");
    String roleName = (String) dataMap.get("role");
    Set<Privilege> privileges = (HashSet) dataMap.get("privilege");

    RoleServiceTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setRoleId(1L);
        role.setRole(roleName);
        role.setPrivilege(privileges);
        roleRepository = Mockito.mock(RoleRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        roleService = new RoleService(roleRepository,userRepository);
    }

    @Test
    @DisplayName("returns_list_of_roles_when_roles_exist")
    public void returns_list_of_roles_when_roles_exist() {
        List<Role> mockRoles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(mockRoles);

        List<Role> roles = roleService.getRoles();

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getRole());
    }

    @Test
    @DisplayName("returns_empty_list_when_no_roles_exist")
    public void returns_empty_list_when_no_roles_exist() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());
        List<Role> roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    @DisplayName("handles_unexpected_exceptions_from_repository")
    public void handles_unexpected_exceptions_from_repository() {
        when(roleRepository.findAll()).thenThrow(new RuntimeException("Unexpected error"));
        Assertions.assertThrows(RuntimeException.class, () -> {
            roleService.getRoles();
        });
    }

    @Test
    @DisplayName("test_add_role_with_valid_privileges")
    public void test_add_role_with_valid_privileges() {
        when(roleRepository.getByRole("Admin")).thenReturn(Optional.empty());
        when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);

        Role result = roleService.addRole(role);

        assertNotNull(result);
        assertEquals("Admin", result.getRole());
        assertTrue(result.getPrivilege().contains(Privilege.GET_ALL_EMPLOYEES));
        assertTrue(result.getPrivilege().contains(Privilege.FIND_EMPLOYEE_BY_ID));
    }

    @Test
    @DisplayName("test_add_role_that_already_exists")
    public void test_add_role_that_already_exists() {
        Role existingRole = role;

        when(roleRepository.getByRole("Admin")).thenReturn(Optional.of(existingRole));
        Role newRole = new Role();
        newRole.setRole("Admin");

        assertThrows(RoleService.RoleAlreadyExistsException.class, () -> {
            roleService.addRole(newRole);
        });
    }

    @Test
    @DisplayName("test_add_role_with_invalid_privilege")
    public void test_add_role_with_invalid_privilege() {
        Role role = new Role();
        role.setRole("SUPERVISOR");

        assertThrows(IllegalArgumentException.class, () -> {
            Privilege invalidPrivilege = Privilege.valueOf("INVALID_PRIVILEGE");
            role.setPrivilege(Set.of(invalidPrivilege));
            roleService.addRole(role);
        });
    }

    @Test
    @DisplayName("test_successfully_updates_role_with_new_privileges")
    public void test_successfully_updates_role_with_new_privileges() {
        Role existingRole = role;

        Role updatedRole = new Role();
        updatedRole.setRoleId(1L);
        updatedRole.setRole("ADMIN");
        updatedRole.setPrivilege(new HashSet<>(Arrays.asList(Privilege.ADD_EMPLOYEE, Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS)));

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));

        roleService.updateRole(updatedRole, 1L);

        Mockito.verify(roleRepository).findById(1L);
        assertTrue(existingRole.getPrivilege().contains(Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS));
    }

    @Test
    @DisplayName("test_clears_existing_privileges_before_adding_new_ones")
    public void test_clears_existing_privileges_before_adding_new_ones() {
        Role existingRole = role;
        Role updatedRole = new Role();
        updatedRole.setRole("ADMIN");
        updatedRole.setPrivilege(new HashSet<>(Arrays.asList(Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS)));

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        roleService.updateRole(updatedRole, 1L);

        Mockito.verify(roleRepository).findById(1L);
        assertEquals(1, existingRole.getPrivilege().size());
        assertTrue(existingRole.getPrivilege().contains(Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS));
    }

    @Test
    @DisplayName("test_throws_entity_not_found_exception_if_role_does_not_exist")
    public void test_throws_entity_not_found_exception_if_role_does_not_exist() {
         Role nonExistentRole = new Role();
        nonExistentRole.setRoleId(1L);
        nonExistentRole.setRole("NON_EXISTENT_ROLE");

        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            roleService.updateRole(nonExistentRole, 1L);
        });
    }

    @Test
    @DisplayName("testDeleteRoleWithExistingRole")
    public void testDeleteRoleWithExistingRole() {
        roleService.userRepository = userRepository;
        Role roleToDelete = role;

        User user = new User();
        user.setUserId(1L);
        user.setRoles(new HashSet<>(Collections.singletonList(roleToDelete)));

        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleToDelete));
        when(userRepository.findByRoles(roleToDelete)).thenReturn(Collections.singletonList(user));

        Role deletedRole = roleService.deleteRole(1L);

        assertNotNull(deletedRole);
        assertEquals(roleToDelete.getRoleId(), deletedRole.getRoleId());
        assertEquals(roleToDelete.getRole(), deletedRole.getRole());
    }

    @Test
    @DisplayName("testDeleteRoleWhenRoleDoesNotExist")
    public void testDeleteRoleWhenRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            roleService.deleteRole(1L);
        });
    }

    @Test
    @DisplayName("testDeleteRoleWhenUserHasNoRoles")
    public void testDeleteRoleWhenUserHasNoRoles() {
        roleService.userRepository = userRepository;
        Role roleToDelete = role;
        User user = new User();
        user.setUserId(1L);
        user.setRoles(new HashSet<>());

        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleToDelete));
        when(userRepository.findByRoles(roleToDelete)).thenReturn(Collections.singletonList(user));

        Role deletedRole = roleService.deleteRole(1L);

        assertNotNull(deletedRole);
        assertEquals(roleToDelete.getRoleId(), deletedRole.getRoleId());
        assertEquals(roleToDelete.getRole(), deletedRole.getRole());

    }

    @Test
    @DisplayName("changeUserRoleTest")
    void changeUserRoleTest() {
        Long userId = 1L;
        Long oldRoleId = 1L;
        Long newRoleId = 2L;

        Role oldRole = new Role();
        oldRole.setRoleId(oldRoleId);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(oldRole);

        User user = new User();
        user.setUserId(userId);
        user.setRoles(userRoles);

        Role newRole = new Role();
        newRole.setRoleId(newRoleId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(oldRoleId)).thenReturn(Optional.of(oldRole));
        when(roleRepository.findById(newRoleId)).thenReturn(Optional.of(newRole));
        Role result = roleService.changeRoleOfUser(userId, oldRoleId, newRoleId);
        assertNotNull(result);
        assertEquals(newRoleId, result.getRoleId());
        assertTrue(user.getRoles().contains(newRole));
        assertFalse(user.getRoles().contains(oldRole));
    }

    @Test
    @DisplayName("changeUserRoleTest")
    void assignRoleToUser() {
        Long userId = 1L;
        Set<Role> userRoles = new HashSet<>();
        User user = new User();
        user.setUserId(userId);
        user.setRoles(userRoles);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));
        Role result = roleService.assignRoleToExistingUser(userId, role.getRoleId());
        assertNotNull(result);
        assertEquals(role.getRoleId(), result.getRoleId());
        assertTrue(user.getRoles().contains(role));
    }
}
