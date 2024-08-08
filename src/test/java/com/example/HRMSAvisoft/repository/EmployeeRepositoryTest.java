package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.controller.JsonReader;
import com.example.HRMSAvisoft.controller.RoleJsonReader;
import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.entity.Privilege;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Skill;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class EmployeeRepositoryTest
{
    @Autowired
    private EmployeeRepository employeeRepository;
    Employee employee;
    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Employee");
    String employeeCode = (String) dataMap.get("employeeCode");
    String firstName = (String) dataMap.get("firstName");
    String lastName = (String) dataMap.get("lastName");
//    String gender = (String) dataMap.get("gender");
    List<Designation> designationList= (List<Designation>) dataMap.get("designationList");
//    List<Skill> skillList= (List<Skill>) dataMap.get("skillList");
//    Map<EmployeeAttribute,String> attributes= (Map<EmployeeAttribute, String>) dataMap.get("attributes");
    EmployeeRepositoryTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeCode(employeeCode);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDesignations(designationList);
//        employee.setSkills(skillList);
//        employee.setAttributes(attributes);
    }

    @Test
    @DisplayName("saveEmployeeTest")
    public void saveEmployeeTest() {
        employeeRepository.save(employee);
        Assertions.assertThat(employee.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(employee.getLastName()).isEqualTo(lastName);
    }

//    @Test
//    @DisplayName("getByEmployee")
//    public void getByEmployee()
//    {
//        List<Employee> employees = employeeRepository.findAll();
//        Employee employeeToFind = employees.get(1);
//        Assertions.assertThat(employee.getEmployee()).isEqualTo(employeeToFind.getEmployee());
//    }

//    @Test
//    @DisplayName("findAllEmployeeTest")
//    public void findAllEmployeeTest()
//    {
//        employeeRepository.save(employee);
//        List<Employee> employees = employeeRepository.findAll();
//        assertThat(employees.get(1).getEmployee()).isEqualTo(employee.getEmployee());
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

}




