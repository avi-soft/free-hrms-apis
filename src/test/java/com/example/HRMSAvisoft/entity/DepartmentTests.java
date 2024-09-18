//package com.example.HRMSAvisoft.entity;
//
//import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class DepartmentTests {
//
//    @Test
//    @DisplayName("test_departmentConstructor")
//    void test_departmentConstructor(){
//        Long departmentId = 1L;
//        String department = "MERN";
//        String description = "MERN department";
//        Set<Organization> organizationSet = new HashSet<Organization>();
//        Organization organization = new Organization();
//        organizationSet.add(organization);
//        Map<DepartmentAttribute, String> attributes = new HashMap<>();
//
//        Employee manager = new Employee();
//
//        Department newDepartment = new Department(departmentId, department, description, attributes, organizationSet, manager);
//
//        assertNotNull(newDepartment);
//        assertEquals(departmentId, newDepartment.getDepartmentId());
//        assertEquals(department, newDepartment.getDepartment());
//        assertEquals(description, newDepartment.getDescription());
//        assertNotNull(newDepartment.getOrganizations());
//    }
//
//    @Test
//    @DisplayName("test_departmentGettersSetters")
//    void testDepartmentGettersSetters(){
//        Long departmentId = 1L;
//        String department = "MERN";
//        String description = "MERN department";
//        Employee manager = new Employee();
//        Organization newOrganization = new Organization();
//
//        Department newDepartment = new Department();
//
//        newDepartment.setDepartmentId( departmentId );
//        newDepartment.setDepartment(department);
//        newDepartment.setDescription( description );
//        newDepartment.setManager( manager );
//        newDepartment.getOrganizations().add(newOrganization);
//
//        assertNotNull(newDepartment);
//        assertEquals(departmentId, newDepartment.getDepartmentId());
//        assertEquals(department, newDepartment.getDepartment());
//        assertEquals(description, newDepartment.getDescription());
//    }
//
//}
