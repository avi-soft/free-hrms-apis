package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Organization;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class OrganizationRepositoryTest {
    @Autowired
    OrganizationRepository organizationRepository;

    String organizationName = "Aviskill";
    String organizationDescription = "Educational";

    Organization newOrganization;

    @BeforeEach
    void setup() throws Exception {
        Organization organization  = new Organization();
        organization.setOrganizationName(organizationName);
        organization.setOrganizationDescription(organizationDescription);

        newOrganization = organizationRepository.save(organization);
    }

    @Test
    @DisplayName("test_saveOrganization")
    void saveOrganization(){

        assertEquals(organizationName, newOrganization.getOrganizationName());
        assertEquals(organizationDescription, newOrganization.getOrganizationDescription());
    }

    @Test
    @DisplayName("test_getOrganizationById")
    void getOrganizationById() throws EntityNotFoundException{
        Organization organizationById = organizationRepository.findById(newOrganization.getOrganizationId()).orElseThrow(()->new EntityNotFoundException("Organization not found"));
        assertEquals(organizationName, organizationById.getOrganizationName());
        assertEquals(organizationDescription, organizationById.getOrganizationDescription());
    }

    @Test
    @DisplayName("test_updateOrganization")
    void updateOrganization() throws EntityNotFoundException{
        Organization organizationById = organizationRepository.findById(newOrganization.getOrganizationId()).orElseThrow(()->new EntityNotFoundException("Organization not found"));

        String updatedOrganizationName = "Avisoft";
        String updatedOrganizationDescription = "Parent Company";

        organizationById.setOrganizationName(updatedOrganizationName);
        organizationById.setOrganizationDescription(updatedOrganizationDescription);
        Organization updatedOrganization = organizationRepository.save(organizationById);

        assertEquals(updatedOrganizationName, updatedOrganization.getOrganizationName());
        assertEquals(updatedOrganizationDescription, updatedOrganization.getOrganizationDescription());

    }

    @Test
    @DisplayName("test_deleteOrganization")
    void deleteOrganization(){

        organizationRepository.delete(newOrganization);
        Organization deletedOrganization = organizationRepository.findById(newOrganization.getOrganizationId()).orElse(null);

        assertNull(deletedOrganization);

    }
}

