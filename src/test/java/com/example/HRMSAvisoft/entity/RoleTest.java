package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTest {

    @Test
    @DisplayName("testRoleGetterSetters1")
    public void testRoleGetterSetter() {
        // Arrange
        Role role = new Role();
        String roleName = "ROLE_ADMIN";

        role.setRole(roleName);
        String retrievedRoleName = role.getRole();

        assertEquals(roleName, retrievedRoleName);
    }
}
