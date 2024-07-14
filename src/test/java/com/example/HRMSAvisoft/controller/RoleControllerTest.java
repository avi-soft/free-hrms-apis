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
                .andExpect(jsonPath("$.message").value("Role updated successfully."))
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
        // Mock the addRole method to return a valid role
        when(roleService.addRole(any(Role.class))).thenReturn(role);

        // Initialize the roleToUpdate with the returned role from addRole
        Role roleToUpdate = roleService.addRole(role);
        roleToUpdate.setRole("Manager");

        doNothing().when(roleService).updateRole(any(Role.class),1L);
        mockMvc.perform(patch("/api/v1/role", roleToUpdate.getRole())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleToUpdate)))
                .andExpect(status().isNoContent());
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

}

