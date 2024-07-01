package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.config.TestSecurityConfig;
import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.service.DepartmentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = TestSecurityConfig.class)
public class DepartmentControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DepartmentService departmentService;

    @MockBean
    DepartmentRepository departmentRepository;

    @MockBean
    EmployeeRepository employeeRepository;
    

    @Value("${getDepartments.url}")
    String getAllDepartmentsUrl;

    @Value("${addDepartment.url}")
    String addDepartmentUrl;

    @Value("${deleteDepartment.url}")
    String deleteDepartmentUrl;


    private String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    @Test
    @DisplayName("test_getAllDepartments")
    void testGetAllDepartments() throws Exception {
        List<Department> departmentsList = new ArrayList<>();
        departmentsList.add(new Department(1L, "Mern", "Mern department", new Employee()));
        departmentsList.add(new Department(2L, "Java", "Java department", new Employee()));

        when(departmentService.getAllDepartments()).thenReturn(departmentsList);

        this.mockMvc.perform(get(getAllDepartmentsUrl))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("test_addDepartment")
    void test_addDepartment() throws Exception {

        String jsonPayload = readFileAsString("src/test/resources/payloads/CreateDepartment.json");
        Employee mockManager = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockManager));

        Department mockDepartment = new Department();

        when(departmentService.addDepartment(any(CreateDepartmentDTO.class))).thenReturn(mockDepartment);

        this.mockMvc.perform(post(addDepartmentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("test_deleteDepartment")
    void testDeleteDepartment() throws Exception {
        Employee manager = new Employee();
        Department mockDepartment = new Department(1L, "DEVOPS", "Devops department at avisoft", manager);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDepartment));

        this.mockMvc.perform(delete(deleteDepartmentUrl, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}
