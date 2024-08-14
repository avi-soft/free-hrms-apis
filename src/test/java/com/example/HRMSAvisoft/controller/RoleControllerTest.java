package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.config.TestSecurityConfig;
import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.repository.RoleRepository;
import com.example.HRMSAvisoft.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
public class RoleControllerTest {
    @MockBean
    RoleService roleService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    Role role,newRole;
    RoleJsonReader jsonReader = new RoleJsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Role");
    String roleName = (String) dataMap.get("role");
    Set<Privilege> privileges = (HashSet) dataMap.get("privilege");

    RoleControllerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setRoleId(1L);
        role.setRole(roleName);
        role.setPrivilege(privileges);
    }

    @Test
    @DisplayName("testCreateRoleWithValidPrivileges")
    @WithMockUser(authorities = "CREATE_ROLE")
    public void testCreateRoleWithValidPrivileges() throws Exception {
        when(roleService.addRole(any(Role.class))).thenReturn(role);

        mockMvc.perform(post("/api/v1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Role created successfully."))
                .andExpect(jsonPath("$.role.role").value(roleName));
    }

    @Test
    @WithMockUser
    @DisplayName("testGetRoles")
    public void testGetRoles() throws Exception {
        List<Role> roles = Arrays.asList(role, role);
        when(roleService.getRoles()).thenReturn(roles);

        mockMvc.perform(get("/api/v1/role"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].role").value(roleName));
    }

    @Test
    @WithMockUser(authorities = "UPDATE_ROLE")
    @DisplayName("testUpdateRole")
    public void testUpdateRole() throws Exception {
        // Given
        Role roleToUpdate = new Role();
        roleToUpdate.setRoleId(1L);
        roleToUpdate.setRole("Manager");
        doNothing().when(roleService).updateRole(any(Role.class), eq(1L));

        mockMvc.perform(patch("/api/v1/role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Role Updated successfully."))
                .andExpect(jsonPath("$.role.role").value("Manager"));
    }

    @Test
    @WithMockUser(authorities = "CREATE_ROLE")
    @DisplayName("testAddRoleWhenRoleAlreadyExists")
    public void testAddRoleWhenRoleAlreadyExists() throws Exception {
        // Mock the service to throw RoleAlreadyExistsException
        when(roleService.addRole(any(Role.class))).thenThrow(new RoleService.RoleAlreadyExistsException("Role already exists"));

        mockMvc.perform(post("/api/v1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Role already exists"));
    }

    @Test
    @WithMockUser(authorities = "CREATE_ROLE")
    @DisplayName("testAddRoleWithInvalidPrivileges")
    public void testAddRoleWithInvalidPrivileges() throws Exception {
        // Mock the service to throw IllegalArgumentException
        when(roleService.addRole(any(Role.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/v1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "DELETE_ROLE")
    @DisplayName("testDeleteRole")
    public void testDeleteRole() throws Exception {
        when(roleService.deleteRole(role.getRoleId())).thenReturn(role);

        // Act & Assert: Perform the DELETE request and verify the response
        mockMvc.perform(delete("/api/v1/role/{roleId}", role.getRoleId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("Role is Deleted successfully"))
                .andExpect(jsonPath("$.data.roleId").value(role.getRoleId()))
                .andExpect(jsonPath("$.data.role").value("Admin"));

        // Verify: Ensure the deleteRole method was called with the correct argument
        verify(roleService, times(1)).deleteRole(role.getRoleId());
    }

    @Test
    @WithMockUser(authorities = "CHANGE_USER_ROLE")
    void changeRoleOfUserTest() throws Exception {
        Long userId = 1L;
        Long oldRoleId = 2L;
        Long newRoleId = 3L;

        Role changedRole = new Role();
        changedRole.setRoleId(newRoleId);
        changedRole.setRole("NewRole");

        when(roleService.changeRoleOfUser(userId, oldRoleId, newRoleId)).thenReturn(changedRole);

        mockMvc.perform(patch("/api/v1/role/changeRole/{userId}/{oldRoleId}/{newRoleId}", userId, oldRoleId, newRoleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("Role of User is changed with new Role"))
                .andExpect(jsonPath("$.data.roleId").value(newRoleId))
                .andExpect(jsonPath("$.data.role").value("NewRole"));

        verify(roleService, times(1)).changeRoleOfUser(userId, oldRoleId, newRoleId);
    }
    @Test
    @WithMockUser(authorities = "ASSIGN_ROLE_TO_USER")
    void assignRoleToUserTest() throws Exception {
        Long userId = 1L;
        when(roleService.assignRoleToExistingUser(userId, role.getRoleId())).thenReturn(role);
        mockMvc.perform(patch("/api/v1/role/{userId}/assignRole/{roleId}", userId, role.getRoleId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("Role is assigned to User successfully"))
                .andExpect(jsonPath("$.data.roleId").value(role.getRoleId()))
                .andExpect(jsonPath("$.data.role").value("Admin"));
        verify(roleService, times(1)).assignRoleToExistingUser(userId, role.getRoleId());
    }

}

