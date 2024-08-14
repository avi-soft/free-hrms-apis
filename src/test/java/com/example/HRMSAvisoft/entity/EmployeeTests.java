package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static graphql.introspection.IntrospectionQueryBuilder.build;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeTests {
    @Test
    @DisplayName("test_Employee_constructor")
    void testEmployeeConstructor(){
        Long empId = 1L;
        String firstName = "test";
        String lastName = "user";
        List<EmergencyContact> emergencyContacts = new ArrayList<EmergencyContact>();
        String employeeCode = "AS01";
        Gender gender = Gender.MALE;
        Department department = new Department();
        Account account = new Account();
        String profileImage = "";
        List<Address> addresses = new ArrayList<Address>();
        List<Performance> performanceList = new ArrayList<Performance>();
        List<LeaveRequest> leaveRequests = new ArrayList<LeaveRequest>();
        List<LeaveBalance> leaveBalanceList = new ArrayList<LeaveBalance>();

        Employee employeeByConstructor =  Employee.builder().employeeId(empId).employeeCode(employeeCode).firstName(firstName).lastName(lastName).emergencyContacts(emergencyContacts).addresses(addresses).gender(gender).account(account).profileImage(profileImage).build();

        assertEquals(empId, employeeByConstructor.getEmployeeId());
        assertEquals(firstName, employeeByConstructor.getFirstName());
        assertEquals(lastName, employeeByConstructor.getLastName());
        assertEquals(gender, employeeByConstructor.getGender());
        assertEquals(account, employeeByConstructor.getAccount());
        assertEquals(profileImage, employeeByConstructor.getProfileImage());
    }

    @Test
    @DisplayName("test_Employee_setters_and_getters")
    void test_Employee_setters_and_getters(){
        Long empId = 1L;
        String firstName = "test";
        String lastName = "user";
        Gender gender = Gender.MALE;
        Account account = new Account();
        List<Address> addresses = new ArrayList<Address>();

        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAddresses(addresses);
        employee.setGender(gender);
        employee.setAccount(account);
        assertEquals(empId, employee.getEmployeeId());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(addresses, employee.getAddresses());
        assertEquals(account, employee.getAccount());
    }
}
