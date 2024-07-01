package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTests {
    @Autowired
    private User user;
    @BeforeEach
    public void setUp(){
        user=new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setCreatedAt("2024-04-24");
    }

    @Test
    @DisplayName("testConstructor 1")
    public void testUserConstructor(){
        Long userId=1L;
        String email="test2@gmail.com";
        String password="test";
        String createdAt="2024-04-24";
        User createdBy=user;
        Role role=new Role();
        role.setRole("HR");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Employee employee=new Employee();

        User user1=new User(userId,email,password,createdAt,createdBy,employee,roles);

        assertEquals(userId, user1.getUserId());
        assertEquals(email, user1.getEmail());
        assertEquals(password, user1.getPassword());
        assertEquals(createdAt, user1.getCreatedAt());
        assertEquals(createdBy, user1.getCreatedBy());
        assertEquals(roles, user1.getRoles());
        assertEquals(employee,user1.getEmployee());
    }

    @Test
    @DisplayName("Setter Test 1")
    public void testSetters() {
        // Arrange
        User user1 = new User();
        Long userId = 1L;
        String email = "test@example.com";
        String password = "password123";
        String createdAt = "2024-04-24";
        User createdBy=user;
        // Act
        user1.setUserId(userId);
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setCreatedAt(createdAt);
        user1.setCreatedBy(createdBy);

        // Assert
        assertEquals(userId, user1.getUserId());
        assertEquals(email, user1.getEmail());
        assertEquals(password, user1.getPassword());
        assertEquals(createdAt, user1.getCreatedAt());
        assertEquals(createdBy,user1.getCreatedBy());
    }
    @Test
    @DisplayName("Getter Test 1")
    public void testGetters() {
        // Arrange
        Long userId = 1L;
        String email = "test@example.com";
        String password = "password123";
        String createdAt = "2024-04-24";
        User createdBy=user;
        Employee employee=new Employee();
        // Create a User object
        User user1 = new User();
        user1.setUserId(userId);
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setCreatedAt(createdAt);
        user1.setCreatedBy(createdBy);
        user1.setEmployee(employee);
        // Act
        Long retrievedUserId = user1.getUserId();
        String retrievedEmail = user1.getEmail();
        String retrievedPassword = user1.getPassword();
        String retrievedCreatedAt = user1.getCreatedAt();
        User retrievedCreatedBy=user1.getCreatedBy();
        // Assert
        assertEquals(userId, retrievedUserId);
        assertEquals(email, retrievedEmail);
        assertEquals(password, retrievedPassword);
        assertEquals(createdAt, retrievedCreatedAt);
        assertEquals(createdBy,retrievedCreatedBy);
    }
}
