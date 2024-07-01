package com.example.HRMSAvisoft.entity;

import org.apache.catalina.Manager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DepartmentTests {

    @Test
    @DisplayName("test_departmentConstructor")
    void test_departmentConstructor(){
        Long departmentId = 1L;
        String department = "MERN";
        String description = "MERN department";

        Employee manager = new Employee();

        Department newDepartment = new Department(departmentId, department, description, manager);

        assertNotNull(newDepartment);
        assertEquals(departmentId, newDepartment.getDepartmentId());
        assertEquals(department, newDepartment.getDepartment());
        assertEquals(description, newDepartment.getDescription());
    }

    @Test
    @DisplayName("test_departmentGettersSetters")
    void testDepartmentGettersSetters(){
        Long departmentId = 1L;
        String department = "MERN";
        String description = "MERN department";
        Employee manager = new Employee();

        Department newDepartment = new Department();

        newDepartment.setDepartmentId( departmentId );
        newDepartment.setDepartment(department);
        newDepartment.setDescription( description );
        newDepartment.setManager( manager );

        assertNotNull(newDepartment);
        assertEquals(departmentId, newDepartment.getDepartmentId());
        assertEquals(department, newDepartment.getDepartment());
        assertEquals(description, newDepartment.getDescription());


    }

}
