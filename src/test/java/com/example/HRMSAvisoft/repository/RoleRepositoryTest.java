package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.controller.JsonReader;
import com.example.HRMSAvisoft.controller.RoleJsonReader;
import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Role;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class RoleRepositoryTest
{
    @Autowired
    private RoleRepository roleRepository;
    Role role;
    RoleJsonReader jsonReader = new RoleJsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Role");
    String roleName = (String) dataMap.get("role");
    Set<Privilege> privileges = (HashSet) dataMap.get("privilege");

    RoleRepositoryTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setRole(roleName);
        role.setPrivilege(privileges);
    }

    @Test
    @DisplayName("saveRoleTest")
    public void saveRoleTest() {
        roleRepository.save(role);
        Assertions.assertThat(role.getRole()).isEqualTo(roleName);
        Assertions.assertThat(role.getPrivilege()).isEqualTo(privileges);
      }

    @Test
    @DisplayName("getByRole")
    public void getByRole()
    {
        List<Role> roles = roleRepository.findAll();
        Role roleToFind = roles.get(0);
        Assertions.assertThat(role.getRole()).isEqualTo(roleToFind.getRole());
    }

    @Test
    @DisplayName("findAllRoleTest")
    public void findAllRoleTest()
    {
        roleRepository.save(role);
        List<Role> roles = roleRepository.findAll();
        assertThat(roles.get(0).getRole()).isEqualTo(role.getRole());
    }

    @Test
    @DisplayName("deleteRole")
    @Transactional
    public void deleteRole()
    {
        Role savedRole = roleRepository.save(role);
        assertNotNull(savedRole);

        // Then, delete the role
        roleRepository.delete(savedRole);

        // Verify that the role no longer exists
        Optional<Role> deletedRole = roleRepository.getByRole(savedRole.getRole());
        assertFalse(deletedRole.isPresent());
    }

}




