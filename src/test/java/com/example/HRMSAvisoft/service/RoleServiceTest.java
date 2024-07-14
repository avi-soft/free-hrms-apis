package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @InjectMocks
    RoleService roleService;

    @Mock
    RoleRepository roleRepository;

    @Test
    @DisplayName("returns_list_of_roles_when_roles_exist")
    public void returns_list_of_roles_when_roles_exist() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        List<Role> mockRoles = Arrays.asList(new Role(1L, "Admin", new HashSet<>()), new Role(2L, "User", new HashSet<>()));
        Mockito.when(roleRepository.findAll()).thenReturn(mockRoles);

        List<Role> roles = roleService.getRoles();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertEquals("Admin", roles.get(0).getRole());
        assertEquals("User", roles.get(1).getRole());
    }

    @Test
    @DisplayName("returns_empty_list_when_no_roles_exist")
    public void returns_empty_list_when_no_roles_exist() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Mockito.when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Role> roles = roleService.getRoles();

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    @DisplayName("handles_unexpected_exceptions_from_repository")
    public void handles_unexpected_exceptions_from_repository() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Mockito.when(roleRepository.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        Assertions.assertThrows(RuntimeException.class, () -> {
            roleService.getRoles();
        });
    }

    @Test
    @DisplayName("test_add_role_with_valid_privileges")
    public void test_add_role_with_valid_privileges() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Role role = new Role();
        role.setRoleId(1L);
        role.setRole("ADMIN");
        role.setPrivilege(Set.of(Privilege.ADD_EMPLOYEE, Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS));

        Mockito.when(roleRepository.getByRole("ADMIN")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);

        Role result = roleService.addRole(role);

        assertNotNull(result);
        assertEquals("ADMIN", result.getRole());
        assertTrue(result.getPrivilege().contains(Privilege.ADD_EMPLOYEE));
        assertTrue(result.getPrivilege().contains(Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS));
    }

    @Test
    @DisplayName("test_add_role_that_already_exists")
    public void test_add_role_that_already_exists() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Role existingRole = new Role();
        existingRole.setRoleId(1L);
        existingRole.setRole("MANAGER");

        Mockito.when(roleRepository.getByRole("MANAGER")).thenReturn(Optional.of(existingRole));

        Role newRole = new Role();
        newRole.setRole("MANAGER");

        assertThrows(RoleService.RoleAlreadyExistsException.class, () -> {
            roleService.addRole(newRole);
        });
    }

    @Test
    @DisplayName("test_add_role_with_invalid_privilege")
    public void test_add_role_with_invalid_privilege() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

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
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Role existingRole = new Role();
        existingRole.setRoleId(1L);
        existingRole.setRole("ADMIN");
        existingRole.setPrivilege(new HashSet<>(Arrays.asList(Privilege.ADD_EMPLOYEE)));

        Role updatedRole = new Role();
        updatedRole.setRoleId(1L);
        updatedRole.setRole("ADMIN");
        updatedRole.setPrivilege(new HashSet<>(Arrays.asList(Privilege.ADD_EMPLOYEE, Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS)));

        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));

        roleService.updateRole(updatedRole, 1L);

        Mockito.verify(roleRepository).findById(1L);
        assertTrue(existingRole.getPrivilege().contains(Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS));
    }

    @Test
    @DisplayName("test_clears_existing_privileges_before_adding_new_ones")
    public void test_clears_existing_privileges_before_adding_new_ones() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Role existingRole = new Role();
        existingRole.setRoleId(1L);
        existingRole.setRole("ADMIN");
        existingRole.setPrivilege(new HashSet<>(Arrays.asList(Privilege.ADD_EMPLOYEE, Privilege.GET_ALL_EMPLOYEES)));

        Role updatedRole = new Role();
        updatedRole.setRole("ADMIN");
        updatedRole.setPrivilege(new HashSet<>(Arrays.asList(Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS)));

        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));

        roleService.updateRole(updatedRole, 1L);

        Mockito.verify(roleRepository).findById(1L);
        assertEquals(1, existingRole.getPrivilege().size());
        assertTrue(existingRole.getPrivilege().contains(Privilege.UPDATE_EMPLOYEE_COMPANY_DETAILS));
    }

    @Test
    @DisplayName("test_throws_entity_not_found_exception_if_role_does_not_exist")
    public void test_throws_entity_not_found_exception_if_role_does_not_exist() {
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Role nonExistentRole = new Role();
        nonExistentRole.setRoleId(1L);
        nonExistentRole.setRole("NON_EXISTENT_ROLE");

        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            roleService.updateRole(nonExistentRole, 1L);
        });
    }
}
