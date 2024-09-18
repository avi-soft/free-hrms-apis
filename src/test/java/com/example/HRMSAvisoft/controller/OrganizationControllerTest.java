//package com.example.HRMSAvisoft.controller;
//import com.example.HRMSAvisoft.config.TestSecurityConfig;
//import com.example.HRMSAvisoft.dto.AddNewOrganizationDTO;
//import com.example.HRMSAvisoft.dto.UpdateOrganizationDTO;
//import com.example.HRMSAvisoft.entity.Organization;
//import com.example.HRMSAvisoft.service.OrganizationService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrganizationController.class)
//@ExtendWith({SpringExtension.class, MockitoExtension.class})
//@ContextConfiguration(classes = TestSecurityConfig.class)
//public class OrganizationControllerTest {
//
//    @MockBean
//    OrganizationService organizationService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    Organization organization;
//
//    JsonReader jsonReader = new JsonReader();
//    Map<String, Object> dataMap = jsonReader.readFile("Organization");
//    String organizationName = (String) dataMap.get("organizationName");
//    String organizationDescription = (String) dataMap.get("organizationDescription");
//
//    OrganizationControllerTest() throws IOException {
//    }
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        organization = new Organization();
//        organization.setOrganizationId(1L);
//        organization.setOrganizationName(organizationName);
//        organization.setOrganizationDescription(organizationDescription);
//    }
//
//    @Test
//    @DisplayName("testCreateOrganizationWithValidPrivileges")
//    @WithMockUser(authorities = "CREATE_ORGANIZATION")
//    public void testCreateOrganizationWithValidPrivileges() throws Exception {
//        when(organizationService.addOrganization(any(AddNewOrganizationDTO.class))).thenReturn(organization);
//
//        mockMvc.perform(post("/api/v1/organization")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(organization)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.isSuccess").value(true))
//                .andExpect(jsonPath("$.message").value("Organization is created successfully"))
//                .andExpect(jsonPath("$.data.organizationName").value(organizationName));
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("testGetOrganizations")
//    public void testGetOrganizations() throws Exception {
//        List<Organization> organizations = Arrays.asList(organization, organization);
//        when(organizationService.getOrganizations()).thenReturn(organizations);
//
//        mockMvc.perform(get("/api/v1/organization"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].organizationName").value(organizationName));
//    }
//
//    @Test
//    @WithMockUser(authorities = "UPDATE_ORGANIZATION")
//    @DisplayName("testUpdateOrganization")
//    public void testUpdateOrganization() throws Exception {
//        Organization updatedOrganization = new Organization();
//        updatedOrganization.setOrganizationId(1L);
//        updatedOrganization.setOrganizationName("Updated Organization");
//        updatedOrganization.setOrganizationDescription("Updated Organization Description");
//
//        UpdateOrganizationDTO organizationDTO = new UpdateOrganizationDTO();
//        organizationDTO.setOrganizationId(1L);
//        organizationDTO.setOrganizationName("Updated Organization");
//        organizationDTO.setOrganizationDescription("Updated Organization Description");
//
//        when(organizationService.updateOrganization(any(UpdateOrganizationDTO.class), eq(1L))).thenReturn(updatedOrganization);
//
//        mockMvc.perform(patch("/api/v1/organization/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(organizationDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.isSuccess").value(true))
//                .andExpect(jsonPath("$.message").value("Organization Updated successfully."))
//                .andExpect(jsonPath("$.data.organizationName").value("Updated Organization"))
//                .andExpect(jsonPath("$.data.organizationDescription").value("Updated Organization Description"));
//    }
//
//    @Test
//    @WithMockUser(authorities = "DELETE_ORGANIZATION")
//    @DisplayName("testDeleteOrganization")
//    public void testDeleteOrganization() throws Exception {
//
//        mockMvc.perform(delete("/api/v1/organization/{organizationId}", organization.getOrganizationId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.isSuccess").value(true))
//                .andExpect(jsonPath("$.message").value("Organization is Deleted successfully"))
//                .andExpect(jsonPath("$.data.organizationId").value(organization.getOrganizationId()))
//                .andExpect(jsonPath("$.data.organizationName").value("Aviskill"));
//
//        verify(organizationService, times(1)).deleteOrganization(organization.getOrganizationId());
//    }
//}
