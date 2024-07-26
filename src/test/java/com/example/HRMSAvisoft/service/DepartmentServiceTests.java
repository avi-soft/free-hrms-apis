package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {

    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    OrganizationRepository organizationRepository;

    @InjectMocks
    DepartmentService departmentService;

    @Test
    @DisplayName("test_returns_list_of_departments_for_valid_organization_id")
    public void test_returns_list_of_departments_for_valid_organization_id() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, null, null);

        List<Department> departmentsList = new ArrayList<>();
        departmentsList.add(new Department(1L, "HR", "Human Resources", new Organization(), new Employee()));
        departmentsList.add(new Department(2L, "IT", "Information Technology", new Organization(), new Employee()));

        when(departmentRepository.findAllByOrganizationId(1L)).thenReturn(departmentsList);

        List<Department> result = departmentService.getAllDepartments(1L);

        assertEquals(2, result.size());
        assertEquals("HR", result.get(0).getDepartment());
        assertEquals("IT", result.get(1).getDepartment());
    }

    @Test
    @DisplayName("test_returns_empty_list_if_no_departments_found")
    public void test_returns_empty_list_if_no_departments_found() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, null, null);

        when(departmentRepository.findAllByOrganizationId(1L)).thenReturn(new ArrayList<>());

        List<Department> result = departmentService.getAllDepartments(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("test_handles_null_organization_id")
    public void test_handles_null_organization_id() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, null, null);

        List<Department> result = departmentService.getAllDepartments(null);

        assertTrue(result.isEmpty());
    }



    @Test
    @DisplayName("test_add_department_success")
    public void test_add_department_success() throws EmployeeNotFoundException, DepartmentService.DepartmentAlreadyExistsException {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO("HR", "Human Resources", 1L, 1L);
        Employee manager = new Employee();
        Organization organization = new Organization();

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        Mockito.when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(new Department());

        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);
        Department result = departmentService.addDepartment(createDepartmentDTO, 1L);

        assertNotNull(result);
    }



    @Test
    @DisplayName("test_employee_not_found_exception")
    public void test_employee_not_found_exception() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO("HR", "Human Resources", 1L, 1L);

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        assertThrows(EmployeeNotFoundException.class, () -> {
            departmentService.addDepartment(createDepartmentDTO, 1L);
        });
    }

    @Test
    @DisplayName("test_organization_not_found_exception")
    public void test_organization_not_found_exception() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO("HR", "Human Resources", 1L, 1L);
        Employee manager = new Employee();

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.empty());

        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        assertThrows(EntityNotFoundException.class, () -> {
            departmentService.addDepartment(createDepartmentDTO, 1L);
        });
    }

    @Test
    @DisplayName("test_update_department_name_and_description")
    public void test_update_department_name_and_description() throws EmployeeNotFoundException, DepartmentService.DepartmentNotFoundException, DepartmentService.DepartmentAlreadyExistsException {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO();
        createDepartmentDTO.setDepartment("New Department Name");
        createDepartmentDTO.setDescription("New Department Description");

        Department department = new Department();
        department.setDepartmentId(1L);
        department.setDepartment("Old Department Name");
        department.setDescription("Old Department Description");

        Mockito.when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        Mockito.when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(department);

        Department updatedDepartment = departmentService.updateDepartment(createDepartmentDTO, 1L);

        assertEquals("New Department Name", updatedDepartment.getDepartment());
        assertEquals("New Department Description", updatedDepartment.getDescription());
    }

    @Test
    @DisplayName("test_update_department_manager")
    public void test_update_department_manager() throws EmployeeNotFoundException, DepartmentService.DepartmentNotFoundException, DepartmentService.DepartmentAlreadyExistsException {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO();
        createDepartmentDTO.setManagerId(2L);

        Department department = new Department();
        department.setDepartmentId(1L);

        Employee manager = new Employee();
        manager.setEmployeeId(2L);

        Mockito.when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        Mockito.when(employeeRepository.findById(2L)).thenReturn(Optional.of(manager));
        Mockito.when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(department);

        Department updatedDepartment = departmentService.updateDepartment(createDepartmentDTO, 1L);

        assertEquals(manager, updatedDepartment.getManager());
    }

    @Test
    @DisplayName("test_department_not_found")
    public void test_department_not_found() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO();

        Mockito.when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> {
            departmentService.updateDepartment(createDepartmentDTO, 1L);
        });
    }

    @Test
    @DisplayName("test_delete_department_success")
    public void test_delete_department_success() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        Department department = new Department();
        department.setDepartmentId(1L);
        Organization organization = new Organization();
        department.setOrganization(organization);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.findByDepartment(department)).thenReturn(new ArrayList<>());

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).delete(department);
    }

    @Test
    @DisplayName("test_remove_department_from_organization")
    public void test_remove_department_from_organization() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        Department department = new Department();
        department.setDepartmentId(1L);
        Organization organization = new Organization();
        organization.getDepartments().add(department);
        department.setOrganization(organization);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.findByDepartment(department)).thenReturn(new ArrayList<>());

        departmentService.deleteDepartment(1L);

        assertFalse(organization.getDepartments().contains(department));
    }

    @Test
    @DisplayName("test_department_not_found_exception")
    public void test_department_not_found_exception() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, organizationRepository);

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> {
            departmentService.deleteDepartment(1L);
        });
    }

}
