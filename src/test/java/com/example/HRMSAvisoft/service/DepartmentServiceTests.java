package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.DepartmentAttributeRepository;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {

    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    OrganizationRepository organizationRepository;
    @Mock
    DepartmentAttributeRepository departmentAttributeRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    DepartmentService departmentService;

    @Test
    @DisplayName("returns_list_of_all_departments_when_present")
    public void returns_list_of_all_departments_when_present() {
        DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, null, null, null);

        List<Department> departments = Arrays.asList(new Department(), new Department());
        Mockito.when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getAllDepartments();

        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("returns_empty_list_when_no_departments")
    public void returns_empty_list_when_no_departments() {
        DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, null, null, null);

        Mockito.when(departmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<Department> result = departmentService.getAllDepartments();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("test_add_department_success")
    public void test_add_department_success() throws EmployeeNotFoundException, EntityNotFoundException, DepartmentAttributeService.DepartmentAlreadyExistsException, AttributeKeyDoesNotExistException {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        DepartmentAttributeRepository departmentAttributeRepository = mock(DepartmentAttributeRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);

        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO();
        createDepartmentDTO.setDepartment("HR");
        createDepartmentDTO.setDescription("Human Resources");
        createDepartmentDTO.setManagerId(1L);
        createDepartmentDTO.setOrganizationId(1L);
        createDepartmentDTO.setAttributes(Map.of("key1", "value1"));

        Employee manager = new Employee();
        Organization organization = new Organization();
        DepartmentAttribute departmentAttribute = new DepartmentAttribute("key1");

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        Mockito.when(departmentAttributeRepository.findByAttributeKey("key1")).thenReturn(Optional.of(departmentAttribute));
        Mockito.when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(new Department());

        DepartmentService departmentService = new DepartmentService(departmentRepository, employeeRepository, departmentAttributeRepository, organizationRepository);
        Department result = departmentService.addDepartment(createDepartmentDTO);

        assertNotNull(result);
    }

    @Test
    @DisplayName("test_attribute_key_does_not_exist_exception")
    public void test_attribute_key_does_not_exist_exception() {
        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO("HR", "Human Resources", 1L, 1L, Map.of("invalidKey", "value"));

        when(departmentAttributeRepository.findByAttributeKey("invalidKey")).thenReturn(Optional.empty());

        assertThrows(AttributeKeyDoesNotExistException.class, () -> {
            departmentService.addDepartment(createDepartmentDTO);
        });
    }

    // Throws EmployeeNotFoundException when managerId does not correspond to an existing employee
    @Test
    public void test_employee_not_found_exception() {
        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO("HR", "Human Resources", 999L, 1L, Map.of("location", "HQ"));

        when(departmentAttributeRepository.findByAttributeKey("location")).thenReturn(Optional.of(new DepartmentAttribute("location")));
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            departmentService.addDepartment(createDepartmentDTO);
        });
    }

    @Test
    @DisplayName("test_department_already_exists_exception")
    public void test_department_already_exists_exception() {
        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO("HR", "Human Resources", 1L, 1L, Map.of("location", "HQ"));

        when(departmentAttributeRepository.findByAttributeKey("location")).thenReturn(Optional.of(new DepartmentAttribute("location")));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
        when(departmentRepository.findByDepartmentAndOrganizationId("HR", 1L)).thenReturn(Optional.of(new Department()));

        assertThrows(DepartmentAttributeService.DepartmentAlreadyExistsException.class, () -> {
            departmentService.addDepartment(createDepartmentDTO);
        });
    }

    @Test
    @DisplayName("test_successful_update")
    public void test_successful_update() throws Exception {
        CreateDepartmentDTO dto = new CreateDepartmentDTO("HR", "Human Resources", 1L, 1L, Map.of("key1", "value1"));
        Department department = new Department(1L, "HR", "Human Resources", new HashMap<>(), new HashSet<>(), null);
        Employee manager = new Employee();
        DepartmentAttribute attribute = new DepartmentAttribute();

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.findByDepartment("HR")).thenReturn(Optional.empty());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(departmentAttributeRepository.findByAttributeKey("key1")).thenReturn(Optional.of(attribute));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department updatedDepartment = departmentService.updateDepartment(dto, 1L);

        assertEquals("HR", updatedDepartment.getDepartment());
        assertEquals("Human Resources", updatedDepartment.getDescription());
        assertEquals(manager, updatedDepartment.getManager());
        assertTrue(updatedDepartment.getAttributes().containsKey(attribute));
    }

    @Test
    @DisplayName("test_department_not_found_exception")
    public void test_department_not_found_exception() {
        CreateDepartmentDTO dto = new CreateDepartmentDTO("HR", "Human Resources", 1L, 1L, Map.of());

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> {
            departmentService.updateDepartment(dto, 1L);
        });
    }

    @Test
    @DisplayName("test_delete_department_success")
    public void test_delete_department_success() {

        Department department = new Department();
        department.setDepartmentId(1L);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.findByDepartment(department)).thenReturn(new ArrayList<>());

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).delete(department);
    }

    @Test
    @DisplayName("test_delete_nonexistent_department")
    public void test_delete_nonexistent_department() {

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> {
            departmentService.deleteDepartment(1L);
        });
    }

    @Test
    @DisplayName("test_remove_department_from_employees")
    public void test_remove_department_from_employees() {

        Department department = new Department();
        department.setDepartmentId(1L);

        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.findByDepartment(department)).thenReturn(employees);

        departmentService.deleteDepartment(1L);

        verify(employeeRepository, times(2)).save(any(Employee.class));
    }

    @Test
    @DisplayName("test_delete_department_no_organizations")
    public void test_delete_department_no_organizations() {

        Department department = new Department();
        department.setDepartmentId(1L);
        department.setOrganizations(new HashSet<>());

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.findByDepartment(department)).thenReturn(new ArrayList<>());

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).delete(department);
    }

}
