package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.config.TestSecurityConfig;
import com.example.HRMSAvisoft.dto.CreatePerformanceDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Performance;
import com.example.HRMSAvisoft.service.PerformanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PerformanceController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = TestSecurityConfig.class)
public class PerformanceControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PerformanceService performanceService;

    @Value("${getPerformanceOfEmployee.url}")
    String getPerformanceOfEmployee;

    @Value("${addPerformanceOfEmployee.url}")
    String addPerformanceOfEmployee;

    @Value("${getAllPerformance.url}")
    String getAllPerformance;

    @Value("${getPerformanceByReviewer.url}")
    String getPerformanceByReviewer;

    @Value("${deletePerformance.url}")
    String deletePerformance;

    private String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    @Test
    @DisplayName("test_getAllPerformanceOfEmployee")
    void test_getAllPerformanceOfEmployee() throws Exception {

        Employee employee = new Employee();
        Employee reviewer = new Employee();

        List<Performance> performanceList = new ArrayList<Performance>();

        Performance performance1 = new Performance();
        performance1.setPerformanceId(1L);
        performance1.setEmployee(employee);
        performance1.setReviewer(reviewer);
        Performance performance2 = new Performance();
        performance2.setPerformanceId(2L);
        performance2.setEmployee(employee);
        performance2.setReviewer(reviewer);
        performanceList.add(performance1);
        performanceList.add(performance2);

        when(performanceService.getAllPerformanceOfEmployee(1L)).thenReturn(performanceList);
        mockMvc.perform(get(getPerformanceOfEmployee, 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("test_addPerformanceOfEmployee")
    void test_addPerformanceOfEmployee()throws Exception {
        Long employeeId = 1L;
        Long reviewerId = 1L;
        Employee employee = new Employee();
        Employee reviewer = new Employee();

        Performance performance1 = new Performance();
        performance1.setPerformanceId(1L);
        performance1.setEmployee(employee);
        performance1.setReviewer(reviewer);

        String jsonPayload = readFileAsString("src/test/resources/payloads/CreatePerformance.json");

        when(performanceService.addPerformanceOfEmployee(eq(employeeId), eq(reviewerId), any(CreatePerformanceDTO.class))).thenReturn(performance1);
        mockMvc.perform(post(addPerformanceOfEmployee)
                        .param("employeeId", employeeId.toString())
                        .param("reviewerId", reviewerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((jsonPayload)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("test_getAllPerformance")
    void testGetAllPerformance() throws Exception {
        Employee employee = new Employee();
        Employee reviewer = new Employee();

        List<Performance> performanceList = new ArrayList<Performance>();

        Performance performance1 = new Performance();
        performance1.setPerformanceId(1L);
        performance1.setEmployee(employee);
        performance1.setReviewer(reviewer);
        Performance performance2 = new Performance();
        performance2.setPerformanceId(2L);
        performance2.setEmployee(employee);
        performance2.setReviewer(reviewer);
        performanceList.add(performance1);
        performanceList.add(performance2);

        when(performanceService.getAllPerformance()).thenReturn(performanceList);
        mockMvc.perform(get(getAllPerformance))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("test_getPerformanceByReviewer")
    void testGetPerformanceByReviewer()throws Exception{

        Long reviewerId = 1L;

        Employee employee = new Employee();
        Employee reviewer = new Employee();

        List<Performance> performanceList = new ArrayList<Performance>();

        Performance performance1 = new Performance();
        performance1.setPerformanceId(1L);
        performance1.setEmployee(employee);
        performance1.setReviewer(reviewer);
        Performance performance2 = new Performance();
        performance2.setPerformanceId(2L);
        performance2.setEmployee(employee);
        performance2.setReviewer(reviewer);
        performanceList.add(performance1);
        performanceList.add(performance2);

        when(performanceService.getPerformanceByReviewer(1L)).thenReturn(performanceList);
        mockMvc.perform(get(getPerformanceByReviewer)
                        .param("reviewerId", reviewerId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("test_delete_performance")
    void testDeletePerformance() throws Exception {

        Long performanceId = 1L;

        mockMvc.perform(delete(deletePerformance)
                        .param("performanceId", performanceId.toString()))
                .andDo(print())
                .andExpect(status().is(204));
    }
}
