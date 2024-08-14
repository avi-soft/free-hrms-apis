package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class DepartmentRepositoryTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    String departmentName = "JAVA";
    String description = "Javas department";
    Employee manager = new Employee();

    Department newDepartment;

    @BeforeEach
    void setup(){
        Department department = new Department();
        department.setDepartment(departmentName);
        department.setDescription(description);
        department.setManager(manager);

        Organization organization = new Organization();
        organization.setOrganizationName("Avisoft");
        organization.setOrganizationDescription("Tech Startup");
        organizationRepository.save(organization);
        department.getOrganizations().add(organization);

        newDepartment = departmentRepository.save(department);
    }
    @Test
    @DisplayName("test_saveDepartment")
    void saveDepartment(){
        assertEquals(departmentName, newDepartment.getDepartment());
        assertEquals(description, newDepartment.getDescription());
    }

    @Test
    @DisplayName("test_getDepartmentById")
    void getDepartmentById(){

        Department departmentFoundById = departmentRepository.findById(newDepartment.getDepartmentId()).orElse(null);

        assertNotNull(departmentFoundById);
        assertEquals(departmentName, departmentFoundById.getDepartment());
        assertEquals(description, departmentFoundById.getDescription());

    }

    @Test
    @DisplayName("test_updateDepartment")
    void updateDepartment()throws EntityNotFoundException{

        Department departmentFoundById = departmentRepository.findById(newDepartment.getDepartmentId()).orElseThrow(()-> new EntityNotFoundException("Department not found"));

        assertNotNull(departmentFoundById);

        String newDepartmentName = "MERN";
        String newDepartmentDescription = "Mern Department";

        departmentFoundById.setDepartment(newDepartmentName);
        departmentFoundById.setDescription(newDepartmentDescription);

        Department updatedDepartment = departmentRepository.save(departmentFoundById);

        assertEquals(newDepartmentName, updatedDepartment.getDepartment());
        assertEquals(newDepartmentDescription, updatedDepartment.getDescription());
    }

    @Test
    @DisplayName("test_deleteDepartment")
    void deleteDepartment(){
        departmentRepository.delete(newDepartment);

        Department deleteDepartment = departmentRepository.findById(newDepartment.getDepartmentId()).orElse((null));

        assertNull(deleteDepartment);

    }
}
