package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.config.TestSecurityConfig;
import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
public class EmployeeControllerTests {

    @MockBean
    EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Employee employee;
    User user;
    Department department;
    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Employee");
    String employeeCode = (String) dataMap.get("employeeCode");
    String firstName = (String) dataMap.get("firstName");
    String lastName = (String) dataMap.get("lastName");
    EmployeeControllerTests() throws IOException {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeCode(employeeCode);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        user = new User();
        user.setUserId(1L);
        user.setEmployee(employee);
        user.setEmail("john.doe@example.com");
//        user.setRoles(Arrays.asList("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("Test searchEmployeesByName with valid privileges")
    @WithMockUser(authorities = "SEARCH_EMPLOYEE_BY_NAME")
    public void testSearchEmployeesByName() throws Exception {
        List<Employee> employees = Arrays.asList(employee, employee);
        when(employeeService.searchEmployeesByName("John")).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employee/searchEmployee?name=John"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].employeeCode").value("EMP001"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].roles[0]").value("ROLE_ADMIN"));
    }

//    @Test
//    @DisplayName("Test searchEmployeeByManagerId with valid privileges")
//    @WithMockUser(authorities = "SEARCH_EMPLOYEE_BY_MANAGER_ID")
//    public void testSearchEmployeeByManagerId() throws Exception {
//        List<Employee> employees = Arrays.asList(employee, employee);
//        when(employeeService.searchEmployeeByManagerId(1L)).thenReturn(employees);
//
//        mockMvc.perform(get("/api/v1/employee/searchByManager?managerId=1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].employeeCode").value("EMP001"))
//                .andExpect(jsonPath("$[0].firstName").value("John"))
//                .andExpect(jsonPath("$[0].lastName").value("Doe"))
//                .andExpect(jsonPath("$[0].userId").value(1))
//                .andExpect(jsonPath("$[0].roles[0]").value("ROLE_ADMIN"));
//    }
//
//    @Test
//    @DisplayName("Test saveEmployeePersonalInfo with valid privileges")
//    @WithMockUser(authorities = "ADD_EMPLOYEE")
//    public void testSaveEmployeePersonalInfo() throws Exception {
//        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
//        createEmployeeDTO.setFirstName("John");
//        createEmployeeDTO.setLastName("Doe");
//        createEmployeeDTO.setEmployeeCode("EMP001");
//        createEmployeeDTO.setJoinDate(LocalDate.now());
//        createEmployeeDTO.setSalary(BigDecimal.valueOf(50000));
//
//        when(employeeService.saveEmployeePersonalInfo(eq(1L), any(CreateEmployeeDTO.class))).thenReturn(employee);
//
//        mockMvc.perform(post("/api/v1/employee/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createEmployeeDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Employee created Successfully"))
//                .andExpect(jsonPath("$.Employee.employeeId").value(1))
//                .andExpect(jsonPath("$.Employee.firstName").value("John"))
//                .andExpect(jsonPath("$.Employee.lastName").value("Doe"));
//    }
//
//    @Test
//    @DisplayName("Test getAllEmployees with valid privileges")
//    @WithMockUser(authorities = "GET_ALL_EMPLOYEES")
//    public void testGetAllEmployees() throws Exception {
//        List<Employee> employees = Arrays.asList(employee, employee);
//        Page<Employee> page = new PageImpl<>(employees);
//
//        when(employeeService.getAllEmployees(any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get("/api/v1/employee/getAllEmployees?page=0&size=5&sortBy=employeeId"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.Employees.length()").value(2))
//                .andExpect(jsonPath("$.currentPage").value(0))
//                .andExpect(jsonPath("$.totalItems").value(2))
//                .andExpect(jsonPath("$.totalPages").value(1))
//                .andExpect(jsonPath("$.message").value("Employees Retrieved Successfully"))
//                .andExpect(jsonPath("$.Success").value(true));
//    }
//
//    @Test
//    @DisplayName("Test getEmployeeById with valid privileges")
//    @WithMockUser(authorities = "FIND_EMPLOYEE_BY_ID")
//    public void testGetEmployeeById() throws Exception {
//        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
//
//        mockMvc.perform(get("/api/v1/employee/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.Employee.employeeId").value(1))
//                .andExpect(jsonPath("$.Employee.firstName").value("John"))
//                .andExpect(jsonPath("$.Employee.lastName").value("Doe"))
//                .andExpect(jsonPath("$.message").value("Employee retrieved Successfully"))
//                .andExpect(jsonPath("$.Status").value(true));
//    }
//
//    @Test
//    @DisplayName("Test updatePersonalDetails with valid privileges")
//    @WithMockUser(authorities = "UPDATE_EMPLOYEE_PERSONAL_DETAILS")
//    public void testUpdatePersonalDetails() throws Exception {
//        UpdatePersonalDetailsDTO updatePersonalDetailsDTO = new UpdatePersonalDetailsDTO();
//        updatePersonalDetailsDTO.setFirstName("Jane");
//        updatePersonalDetailsDTO.setLastName("Doe");
//
//        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
//        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(employee);
//
//        mockMvc.perform(put("/api/v1/employee/updatePersonalDetails/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatePersonalDetailsDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.UpdatedEmployee.employeeId").value(1))
//                .andExpect(jsonPath("$.UpdatedEmployee.firstName").value("Jane"))
//                .andExpect(jsonPath("$.UpdatedEmployee.lastName").value("Doe"))
//                .andExpect(jsonPath("$.message").value("Personal Details Updated"))
//                .andExpect(jsonPath("$.Status").value(true));
//    }
//
//    @Test
//    @DisplayName("Test updateEmployeeDetails with valid privileges")
//    @WithMockUser(authorities = "UPDATE_EMPLOYEE_COMPANY_DETAILS")
//    public void testUpdateEmployeeDetails() throws Exception {
//        UpdateEmployeeDetailsDTO updateEmployeeDetailsDTO = new UpdateEmployeeDetailsDTO();
//        updateEmployeeDetailsDTO.setFirstName("Jane");
//        updateEmployeeDetailsDTO.setLastName("Doe");
//        Map<String, String> attributes = new HashMap<>();
//        attributes.put("Designation", "Manager");
//        updateEmployeeDetailsDTO.setAttributes(attributes);
//
//        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
//        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(employee);
//
//        mockMvc.perform(put("/api/v1/employee/updateEmployeeDetails/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateEmployeeDetailsDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.UpdatedEmployee.employeeId").value(1))
//                .andExpect(jsonPath("$.UpdatedEmployee.firstName").value("Jane"))
//                .andExpect(jsonPath("$.UpdatedEmployee.lastName").value("Doe"))
//                .andExpect(jsonPath("$.message").value("Personal Details Updated"))
//                .andExpect(jsonPath("$.Status").value(true));
//    }

    // Add more test cases as per your requirements
}