//package com.example.HRMSAvisoft.repository;
//
//import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
//import com.example.HRMSAvisoft.controller.JsonReader;
//import com.example.HRMSAvisoft.controller.RoleJsonReader;
//import com.example.HRMSAvisoft.entity.*;
//import jakarta.transaction.Transactional;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.io.IOException;
//import java.util.*;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//@Transactional
//class EmployeeRepositoryTest
//{
//    @Autowired
//    private EmployeeRepository employeeRepository;
//    Employee employee;
//    Department department;
//    JsonReader jsonReader = new JsonReader();
//    Map<String, Object> dataMap = jsonReader.readFile("Employee");
//    String employeeCode = (String) dataMap.get("employeeCode");
//    String firstName = (String) dataMap.get("firstName");
//    String lastName = (String) dataMap.get("lastName");
//    EmployeeRepositoryTest() throws IOException {
//    }
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        employee = new Employee();
//        employee.setEmployeeId(1L);
//        employee.setEmployeeCode(employeeCode);
//        employee.setFirstName(firstName);
//        employee.setLastName(lastName);
//    }
//
//    @Test
//    @DisplayName("saveEmployeeTest")
//    public void saveEmployeeTest() {
//        employeeRepository.save(employee);
//        Assertions.assertThat(employee.getFirstName()).isEqualTo(firstName);
//        Assertions.assertThat(employee.getLastName()).isEqualTo(lastName);
//    }
//
//    @Test
//    @DisplayName("deleteEmployee")
//    public void deleteEmployee()
//    {
//        Employee savedEmployee = employeeRepository.save(employee);
//        assertNotNull(savedEmployee);
//
//        // Then, delete the employee
//        employeeRepository.delete(savedEmployee);
//
//        // Verify that the employee no longer exists
//        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getEmployeeId());
//        assertFalse(deletedEmployee.isPresent());
//    }
//
//    @Test
//    @DisplayName("getByEmployeeIdTest")
//    public void getByEmployeeIdTest() {
//        employeeRepository.save(employee);
//        Employee foundEmployee = employeeRepository.getByEmployeeId(employee.getEmployeeId());
//        assertNotNull(foundEmployee);
//        Assertions.assertThat(foundEmployee.getEmployeeCode()).isEqualTo(employeeCode);
//    }
//
//    @Test
//    @DisplayName("searchEmployeesByNameTest")
//    public void searchEmployeesByNameTest() {
//        employeeRepository.save(employee);
//        List<Employee> foundEmployees = employeeRepository.searchEmployeesByName(firstName + " " + lastName);
//        assertFalse(foundEmployees.isEmpty());
//        assertEquals(foundEmployees.get(0).getFirstName(), firstName);
//        assertEquals(foundEmployees.get(0).getLastName(), lastName);
//    }
//
//    @Test
//    @DisplayName("findTopByOrderByEmployeeCodeDescTest")
//    public void findTopByOrderByEmployeeCodeDescTest() {
//        Employee anotherEmployee = new Employee();
//        anotherEmployee.setEmployeeId(2L);
//        anotherEmployee.setEmployeeCode("EMP999");
//        anotherEmployee.setFirstName("Jane");
//        anotherEmployee.setLastName("Doe");
//        anotherEmployee.setDepartment(department);
//
//        employeeRepository.save(employee);
//        employeeRepository.save(anotherEmployee);
//
//        Employee foundEmployee = employeeRepository.findTopByOrderByEmployeeCodeDesc();
//        assertNotNull(foundEmployee);
//        Assertions.assertThat(foundEmployee.getEmployeeCode()).isEqualTo("EMP999");
//    }
//
//    @Test
//    @DisplayName("existsByEmployeeCodeTest")
//    public void existsByEmployeeCodeTest() {
//        employeeRepository.save(employee);
//        boolean exists = employeeRepository.existsByEmployeeCode(employeeCode);
//        assertTrue(exists);
//    }
//}
//
//
//
//
