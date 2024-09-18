//package com.example.HRMSAvisoft.controller;
//
//import com.example.HRMSAvisoft.config.TestSecurityConfig;
//import com.example.HRMSAvisoft.entity.Designation;
//import com.example.HRMSAvisoft.repository.DesignationRepository;
//import com.example.HRMSAvisoft.service.DesignationService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(DesignationController.class)
//@ExtendWith({SpringExtension.class, MockitoExtension.class})
//@ActiveProfiles("test")
//@ContextConfiguration(classes = TestSecurityConfig.class)
//public class DesignationControllerTests {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    DesignationService designationService;
//
//    @MockBean
//    DesignationRepository designationRepository;
//
//
//    @Value("${getAndAddDesignations.url}")
//    String getAndAddDesignationsUrl;
//
//    @Value("${updateAndDeleteDesignation.url}")
//    String updateAndDeleteDesignationUrl;
//
//
//    private String readFileAsString(String file) throws Exception {
//        return new String(Files.readAllBytes(Paths.get(file)));
//    }
//
//    @Test
//    @DisplayName("testGetAllDesignations")
//    void getAllDesignations() throws Exception {
//        List<Designation> designationList = new ArrayList<Designation>();
//
//        designationList.add(new Designation(1L, "CEO"));
//        designationList.add(new Designation(2L, "HR"));
//
//        when(designationService.getAllDesignations()).thenReturn(designationList);
//        this.mockMvc.perform(get(getAndAddDesignationsUrl))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("testAddDesignation")
//    void testAddDesignation() throws Exception {
//
//        String jsonPayload = readFileAsString("src/test/resources/payloads/CreateDesignation.json");
//
//        Designation mockDesignation = new Designation();
//        mockDesignation.setDesignation("HR");
//
//        when(designationService.addDesignation(any(Designation.class))).thenReturn(mockDesignation);
//
//        this.mockMvc.perform(post(getAndAddDesignationsUrl)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonPayload))
//                .andDo(print())
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @DisplayName("testUpdateDesignation")
//    void testUpdateDesignation() throws Exception {
//        String jsonPayload = readFileAsString("src/test/resources/payloads/CreateDesignation.json");
//
//        Designation mockDesignation = new Designation();
//        mockDesignation.setDesignation("HR");
//
//
//        this.mockMvc.perform(patch(updateAndDeleteDesignationUrl, 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonPayload))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("testdeleteDesignation")
//    void testDeleteDesignation() throws Exception {
//
//
//
//        Designation mockDesignation = new Designation(1L, "HR");
//        when(designationRepository.findById(1L)).thenReturn(Optional.of(mockDesignation));
//
//        this.mockMvc.perform(delete(updateAndDeleteDesignationUrl, 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().is2xxSuccessful());
//    }
//}
