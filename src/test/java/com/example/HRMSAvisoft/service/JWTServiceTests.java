package com.example.HRMSAvisoft.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static com.example.HRMSAvisoft.entity.Privilege.*;
import static org.junit.jupiter.api.Assertions.*;

public class JWTServiceTests {


    private static final long EXPIRATION_TIME_MILLIS = 86400000; // 1 day in milliseconds

    @InjectMocks
    private JWTService jwtService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

@Test
@DisplayName("test_generate_valid_token")
public void test_generate_valid_token() {
    Long userId = 123L;
    Set<Role> roles = new HashSet<>();


    // Create a Role with privileges
    Role adminRole = Role.builder()
            .role("Admin")
            .privilege(new HashSet<>(Arrays.asList(ADD_EMPLOYEE, FIND_EMPLOYEE_BY_ID, SEARCH_EMPLOYEE_BY_NAME)))
            .build();

    roles.add(adminRole);

    String jwt = JWTService.createJWT(userId, roles);

    assertNotNull(jwt);
}

    @Test
    @DisplayName("test_expiry_time_correct")
    public void test_expiry_time_correct() {
        Long userId = 123L;
        Set<Role> roles = new HashSet<>();

        Role adminRole = Role.builder()
                .role("Admin")
                .privilege(new HashSet<>(Arrays.asList(ADD_EMPLOYEE, FIND_EMPLOYEE_BY_ID, SEARCH_EMPLOYEE_BY_NAME)))
                .build();
        roles.add(adminRole);

        String jwt = JWTService.createJWT(userId, roles);

        DecodedJWT decodedJWT = JWTService.retrieveJWT(jwt);
        Date expiresAt = decodedJWT.getExpiresAt();

        assertNotNull(expiresAt);
        assertEquals(EXPIRATION_TIME_MILLIS, expiresAt.getTime() - decodedJWT.getIssuedAt().getTime());
    }

    @Test
    @DisplayName("test_empty_roles")
    public void test_empty_roles() {
        Long userId = 123L;
        Set<Role> roles = new HashSet<>();

        String jwt = JWTService.createJWT(userId, roles);

        DecodedJWT decodedJWT = JWTService.retrieveJWT(jwt);
        List<String> retrievedRoles = JWTService.retrieveRoles(jwt);

        assertNotNull(decodedJWT);
        assertNotNull(retrievedRoles);
        assertTrue(retrievedRoles.isEmpty());
    }

    @Test
    @DisplayName("test_invalid_jwt_string")
    public void test_invalid_jwt_string() {
        String jwtString = "invalid_jwt_string";

        assertThrows(JWTVerificationException.class, () -> {
            JWTService.retrieveJWT(jwtString);
        });
    }

    @Test
    @DisplayName("test_null_jwt_string")
    public void test_null_jwt_string() {
        String jwtString = null;

        assertThrows(JWTDecodeException.class, () -> {
            JWTService.retrieveJWT(jwtString);
        });
    }

    @Test
    @DisplayName("test_RetrieveUserId")
    public void testRetrieveUserId() {

        Long userId = 123L;
        Set<Role> roles = new HashSet<>();

        Role adminRole = Role.builder()
                .role("Admin")
                .privilege(new HashSet<>(Arrays.asList(ADD_EMPLOYEE, FIND_EMPLOYEE_BY_ID, SEARCH_EMPLOYEE_BY_NAME)))
                .build();
        roles.add(adminRole);

        String jwt = jwtService.createJWT(userId, roles);

        Long userIdFromToken = jwtService.retrieveUserId(jwt);

        assertEquals(userId, userIdFromToken);
    }

    @Test
    @DisplayName("test_RetrievePrivileges")
    public void testRetrievePrivileges() {

        Long userId = 123L;
        Set<Role> roles = new HashSet<>();

        Role adminRole = Role.builder()
                .role("Admin")
                .privilege(new HashSet<>(Arrays.asList(ADD_EMPLOYEE)))
                .build();
        roles.add(adminRole);

        String jwt = jwtService.createJWT(userId, roles);


        List<String> privilegesFromToken = jwtService.retrieveRoles(jwt);
        assertEquals("ADD_EMPLOYEE", privilegesFromToken.get(0));
    }

}