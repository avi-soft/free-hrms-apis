//package com.example.HRMSAvisoft.entity;
//
//import com.example.HRMSAvisoft.controller.JsonReader;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//import java.io.IOException;
//import java.util.*;
//
//@ExtendWith(MockitoExtension.class)
//class OrganizationTest {
//    private Organization organization;
//    @Mock
//    private Department mockDepartment;
//    @Mock
//    private User mockUser;
//    List<Department> departments;
//    List<User> userList;
//
//    JsonReader jsonReader = new JsonReader();
//    Map<String, Object> dataMap = jsonReader.readFile("Organization");
//
//    String organizationName = (String) dataMap.get("organizationName");
//    String organizationDescription = (String) dataMap.get("organizationDescription");
//    OrganizationTest() throws IOException {
//    }
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        organization= new Organization();
//        organization.setOrganizationId(1L);
//        organization.setOrganizationName(organizationName);
//        organization.setOrganizationDescription(organizationDescription);
//
//        departments = new ArrayList<>();
//        departments.add(mockDepartment);
//        organization.setDepartments(departments);
//
//        userList= new ArrayList<>();
//        userList.add(mockUser);
//        organization.setUsers(userList);
//    }
//
//    @Test
//    void testConstructor() {
//        Assertions.assertEquals(1L, organization.getOrganizationId());
//        Assertions.assertEquals(organizationName, organization.getOrganizationName());
//        Assertions.assertEquals(organizationDescription, organization.getOrganizationDescription());
//        Assertions.assertEquals(departments, organization.getDepartments());
//        Assertions.assertEquals(userList, organization.getUsers());
//    }
//
//    @Test
//    void testGetters() {
//        Assertions.assertEquals(1L, organization.getOrganizationId());
//        Assertions.assertEquals(organizationName, organization.getOrganizationName());
//        Assertions.assertEquals(organizationDescription, organization.getOrganizationDescription());
//        Assertions.assertEquals(departments, organization.getDepartments());
//        Assertions.assertEquals(userList, organization.getUsers());
//    }
//
//    @Test
//    void testSetters() {
//        organization.setOrganizationId(2L);
//        organization.setOrganizationName("Updated Organization");
//        organization.setOrganizationDescription("This is an updated organization");
//        organization.setDepartments(departments);
//        organization.setUsers(userList);
//        Assertions.assertEquals(2L, organization.getOrganizationId());
//        Assertions.assertEquals("Updated Organization", organization.getOrganizationName());
//        Assertions.assertEquals("This is an updated organization", organization.getOrganizationDescription());
//        Assertions.assertEquals(departments, organization.getDepartments());
//        Assertions.assertEquals(userList, organization.getUsers());
//    }
//}