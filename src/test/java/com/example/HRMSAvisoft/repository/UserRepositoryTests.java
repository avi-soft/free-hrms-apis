package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    User testuser1=new User() ;

    @BeforeEach
    public void setUp() {
        // Insert test data into the database
        User user1=new User();
        user1.setUserId(1L);
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setCreatedAt(LocalDateTime.now().toString());
       testuser1 =userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setCreatedAt(LocalDateTime.now().toString());
        userRepository.save(user2);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test
        userRepository.deleteAll();
    }

//    @Test
////   @Transactional
//    public void testSaveUser() {
//
//        Role role = new Role();
//        role.setRole("ROLE_USER");
//        Set<Role> roles = new HashSet<>();
//        roles.add(role);
//        User user = new User();
//        user.setEmail("john.doe@example.com");
//        user.setPassword("password");
//        user.setCreatedAt(LocalDateTime.now().toString());
//        user.setCreatedBy(null);
//
//        user.setRoles(roles);
//
//        // When
//        User savedUser = userRepository.save(user);
//
//        // Then
//        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getUserId()).isNotNull();
//        assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
//        assertThat(savedUser.getPassword()).isEqualTo("password");
//    }

    @Test
    @Transactional
    public void testGetByEmail() {
        // Retrieve a user from the database by email
        User user = userRepository.getByEmail("user1@example.com");

        // Verify that the user exists
        assertNotNull(user);
        assertEquals("user1@example.com", user.getEmail());
        assertEquals("password1", user.getPassword());
    }

    @Test
    public void testGetByUserId() {
        // Retrieve a user from the database by user ID
        User user = userRepository.getByUserId(testuser1.getUserId());

        // Verify that the user exists
        assertNotNull(user);
        assertEquals("user1@example.com", user.getEmail());
        assertEquals("password1", user.getPassword());
    }

}
