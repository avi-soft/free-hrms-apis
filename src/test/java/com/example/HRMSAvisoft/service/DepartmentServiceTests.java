package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentService departmentService;

    @Test
    @DisplayName("test_getAllEmergencyContacts")
    void testGetAllEmergencyContacts(){
        List<Department> expectedDepartments = new ArrayList<>();
        expectedDepartments.add(new Department(1L, "Department 1", "Description 1", new Employee()));
        expectedDepartments.add(new Department(2L, "Department 2", "Description 2", new Employee()));
        expectedDepartments.add(new Department(3L, "Department 3", "Description 3", new Employee()));

        when(departmentRepository.findAll()).thenReturn(expectedDepartments);

        List<Department> actualDepartments = departmentService.getAllDepartments();

        assertEquals(expectedDepartments, actualDepartments);
    }

    @Test
    @DisplayName("test_addDepartment_validInput")
    public void test_addDepartment_validInput() throws EmployeeNotFoundException {

        EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
        DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO();
        createDepartmentDTO.setDepartment("Test Department");
        createDepartmentDTO.setDescription("Test Description");
        createDepartmentDTO.setManagerId(1L);

        Employee manager = new Employee();
        manager.setEmployeeId(1L);

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
        Mockito.when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(new Department());

        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository);

        Department result = departmentService.addDepartment(createDepartmentDTO);

        assertNotNull(result);
    }

    @Test
    public void test_update_department_name_and_description() throws EmployeeNotFoundException {
        // Create mock objects
        DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

        // Create DepartmentService object with mock repositories
        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository);

        // Create test data
        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO();
        createDepartmentDTO.setDepartment("New Department Name");
        createDepartmentDTO.setDescription("New Department Description");

        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartment("Old Department Name");
        department.setDescription("Old Department Description");

        // Mock repository methods
        Mockito.when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        Mockito.when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(department);

        // Call the method under test
        Department updatedDepartment = departmentService.updateDepartment(createDepartmentDTO, 1L);

        // Assert the result
        assertEquals("New Department Name", updatedDepartment.getDepartment());
        assertEquals("New Department Description", updatedDepartment.getDescription());
    }


}
