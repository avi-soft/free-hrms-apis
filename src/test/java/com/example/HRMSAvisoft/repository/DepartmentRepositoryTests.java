package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class DepartmentRepositoryTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    String departmentName = "JAVA";
    String description = "Javas department";
    Employee manager = new Employee();

    @Test
    @DisplayName("test_saveDepartment")
    void saveDepartment(){
        Department department = new Department();
        department.setDepartment(departmentName);
        department.setDescription(description);
        department.setManager(manager);

        Department newDepartment = departmentRepository.save(department);

        assertEquals(departmentName, newDepartment.getDepartment());
        assertEquals(description, newDepartment.getDescription());
    }

    @Test
    @DisplayName("test_getDepartmentById")
    void getDepartmentById(){
        Department department = new Department();
        department.setDepartment(departmentName);
        department.setDescription(description);
        department.setManager(manager);

        Department newDepartment = departmentRepository.save(department);

        Department departmentFoundById = departmentRepository.findById(newDepartment.getDepartmentId()).orElse(null);

        assertNotNull(departmentFoundById);
        assertEquals(departmentName, departmentFoundById.getDepartment());
        assertEquals(description, departmentFoundById.getDescription());

    }

    @Test
    @DisplayName("test_updateDepartment")
    void updateDepartment(){
        Department department = new Department();
        department.setDepartment(departmentName);
        department.setDescription(description);
        department.setManager(manager);

        Department newDepartment = departmentRepository.save(department);

        Department departmentFoundById = departmentRepository.findById(newDepartment.getDepartmentId()).orElse(null);

        assertNotNull(departmentFoundById);

        String newDepartmentName = "MERN";
        String newDepartmentDescription = "Mern Department";

        departmentFoundById.setDepartment(newDepartmentName);
        departmentFoundById.setDescription(newDepartmentDescription);

        Department updatedDepartment = departmentRepository.save(departmentFoundById);

        assertEquals(newDepartmentName, updatedDepartment.getDepartment());
        assertEquals(newDepartmentDescription, updatedDepartment.getDescription());
    }
}
