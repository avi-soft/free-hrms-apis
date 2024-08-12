package com.example.HRMSAvisoft.service;
import com.cloudinary.Cloudinary;
import com.example.HRMSAvisoft.controller.JsonReader;
import com.example.HRMSAvisoft.dto.AddNewOrganizationDTO;
import com.example.HRMSAvisoft.dto.UpdateOrganizationDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.repository.OrganizationAttributeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {
    @InjectMocks
    OrganizationService organizationService;

    @Mock
    OrganizationRepository organizationRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    Cloudinary cloudinary;
    @Mock
    OrganizationAttributeRepository organizationAttributeRepository;

    Organization organization;
    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Organization");
    String organizationName = (String) dataMap.get("organizationName");
    String organizationDescription = (String) dataMap.get("organizationDescription");

    OrganizationServiceTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        organization = new Organization();
        organization.setOrganizationId(1L);
        organization.setOrganizationName(organizationName);
        organization.setOrganizationDescription(organizationDescription);
        organizationService = new OrganizationService(organizationRepository, modelMapper, cloudinary,organizationAttributeRepository);
    }

    @Test
    public void test_retrieve_departments_for_existing_organization() {
        OrganizationRepository organizationRepository = Mockito.mock(OrganizationRepository.class);
        Organization organization = new Organization();
        Department department1 = new Department();
        Department department2 = new Department();
        organization.setDepartments(Set.of(department1, department2));
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        OrganizationService organizationService = new OrganizationService(organizationRepository, new ModelMapper(), null, null);
        List<Department> departments = organizationService.getDepartmentsOfOrganization(1L);

        Assertions.assertEquals(2, departments.size());
        Assertions.assertTrue(departments.contains(department1));
        Assertions.assertTrue(departments.contains(department2));
    }

    @Test
    public void test_return_empty_list_if_no_departments() {
        OrganizationRepository organizationRepository = Mockito.mock(OrganizationRepository.class);
        Organization organization = new Organization();
        organization.setDepartments(Collections.emptySet());
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        OrganizationService organizationService = new OrganizationService(organizationRepository, new ModelMapper(), null, null);
        List<Department> departments = organizationService.getDepartmentsOfOrganization(1L);

        Assertions.assertTrue(departments.isEmpty());
    }

    @Test
    @DisplayName("returns_list_of_organizations_when_organizations_exist")
    public void returns_list_of_organizations_when_organizations_exist() {
        List<Organization> mockOrganizations = Arrays.asList(organization);
        when(organizationRepository.findAll()).thenReturn(mockOrganizations);
        List<Organization> organizations = organizationService.getOrganizations();

        assertNotNull(organizations);
        assertEquals(1, organizations.size());
        assertEquals("Aviskill", organizations.get(0).getOrganizationName());
    }

    @Test
    @DisplayName("returns_empty_list_when_no_organizations_exist")
    public void returns_empty_list_when_no_organizations_exist() {
        when(organizationRepository.findAll()).thenReturn(Collections.emptyList());
        List<Organization> organizations = organizationService.getOrganizations();
        assertNotNull(organizations);
        assertTrue(organizations.isEmpty());
    }

    @Test
    @DisplayName("handles_unexpected_exceptions_from_repository")
    public void handles_unexpected_exceptions_from_repository() {
        when(organizationRepository.findAll()).thenThrow(new RuntimeException("Unexpected error"));
        Assertions.assertThrows(RuntimeException.class, () -> {
            organizationService.getOrganizations();
        });
    }

    @Test
    @DisplayName("test_add_organization")
    public void test_add_organization() {
        AddNewOrganizationDTO organizationDTO = new AddNewOrganizationDTO();
        organizationDTO.setOrganizationName(organizationName);
        organizationDTO.setOrganizationDescription(organizationDescription);

        when(organizationRepository.getByOrganizationName("Aviskill")).thenReturn(Optional.empty());
        when(modelMapper.map(Mockito.any(AddNewOrganizationDTO.class), Mockito.eq(Organization.class)))
                .thenReturn(organization);
        when(organizationRepository.save(Mockito.any(Organization.class))).thenReturn(organization);
        Organization result = organizationService.addOrganization(organizationDTO);

        assertNotNull(result);
        assertEquals("Aviskill", result.getOrganizationName());
    }

    @Test
    @DisplayName("test_add_organization_that_already_exists")
    public void test_add_organization_that_already_exists() {
        Organization existingOrganization = organization;

        when(organizationRepository.getByOrganizationName("Aviskill")).thenReturn(Optional.of(existingOrganization));
        AddNewOrganizationDTO organizationDTO = new AddNewOrganizationDTO();
        organizationDTO.setOrganizationName(organizationName);
        organizationDTO.setOrganizationDescription(organizationDescription);
        when(modelMapper.map(Mockito.any(AddNewOrganizationDTO.class), Mockito.eq(Organization.class)))
                .thenReturn(existingOrganization);

        assertThrows(OrganizationService.OrganizationAlreadyExistsException.class, () -> {
            organizationService.addOrganization(organizationDTO);
        });
    }

    @Test
    @DisplayName("test_successfully_updates_organization")
    public void test_successfully_updates_organization() {
        Organization existingOrganization = organization;
        UpdateOrganizationDTO organizationDTO = new UpdateOrganizationDTO();
        organizationDTO.setOrganizationName("Updated Organization");
        organizationDTO.setOrganizationDescription("Updation of new Organization");
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(existingOrganization));
        when(organizationRepository.save(Mockito.any(Organization.class))).thenReturn(organization);

        Organization result = organizationService.updateOrganization(organizationDTO, 1L);
        assertEquals("Updated Organization", result.getOrganizationName());
        assertEquals("Updation of new Organization", result.getOrganizationDescription());
    }

    @Test
    @DisplayName("testDeleteOrganizationWithExistingOrganization")
    public void testDeleteOrganizationWithExistingOrganization() {
        Organization organizationToDelete = organization;

        // Mock the findById method to return the organization to be deleted
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organizationToDelete));

        // Call the deleteOrganization method
        Organization deletedOrganization = organizationService.deleteOrganization(1L);

        // Verify the interactions and assertions
        assertNotNull(deletedOrganization);
        assertEquals(organizationToDelete.getOrganizationId(), deletedOrganization.getOrganizationId());
        assertEquals(organizationToDelete.getOrganizationName(), deletedOrganization.getOrganizationName());

        // Verify that the organizationRepository delete method was called with the correct organization
        Mockito.verify(organizationRepository).delete(organizationToDelete);
    }

    @Test
    @DisplayName("testDeleteOrganizationWhenOrganizationDoesNotExist")
    public void testDeleteOrganizationWhenOrganizationDoesNotExist() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            organizationService.deleteOrganization(1L);
        });
    }

}



