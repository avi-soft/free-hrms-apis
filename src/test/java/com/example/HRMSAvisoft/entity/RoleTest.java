package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    @DisplayName("test_RoleConstructor")
    public void testRoleConstructor(){
        Long roleId = 1L;
        String role = "Admin";
        Set<Privilege> privileges = new HashSet<Privilege>();
        Privilege newPrivilege = Privilege.CREATE_NEW_USER;
        Privilege newPrivilege2 = Privilege.ADD_EMPLOYEE;
        privileges.add(newPrivilege);
        privileges.add(newPrivilege2);

        Role newRole = new Role(roleId, role, privileges);

        assertEquals(role, newRole.getRole());
        assertTrue(newRole.getPrivilege().contains(newPrivilege));
        assertTrue(newRole.getPrivilege().contains(newPrivilege2));

    }

    @Test
    @DisplayName("testRoleGetterSetters1")
    public void testRoleGetterSetter() {

        Role newRole = new Role();
        String roleName = "Admin";

        Set<Privilege> privileges = new HashSet<Privilege>();
        Privilege newPrivilege = Privilege.CREATE_NEW_USER;
        Privilege newPrivilege2 = Privilege.ADD_EMPLOYEE;

        privileges.add(newPrivilege);
        privileges.add(newPrivilege2);


        newRole.setRole(roleName);
        newRole.setPrivilege(privileges);

        String retrievedRoleName = newRole.getRole();
        assertEquals(roleName, retrievedRoleName);
        assertTrue(newRole.getPrivilege().contains(newPrivilege));
        assertTrue(newRole.getPrivilege().contains(newPrivilege2));


    }
}