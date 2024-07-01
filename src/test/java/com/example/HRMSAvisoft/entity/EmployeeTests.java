package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTests {


    @Test
    @DisplayName("test_Employee_constructor")
    void testEmployeeConstructor(){
        Long empId = 1L;
        String firstName = "test";
        String lastName = "user";
        String contact = "8539228375";
        List<EmergencyContact> emergencyContacts = new ArrayList<EmergencyContact>();
        BigDecimal salary = new BigDecimal(20000);
        Position position = Position.TESTER;
        String adhaarNumber = "9274270520809";
        String panNumber = "2492090ur902";
        String uanNumber = "30ur039ur0";
        String employeeCode = "AS01";
        Gender gender = Gender.MALE;
        String joinDate = "2022/12/12";
        Department department = new Department();
        Account account = new Account();
        String dateOfBirth = "2004/12/22";
        String profileImage = "";
        List<Address> addresses = new ArrayList<Address>();
        List<Performance> performanceList = new ArrayList<Performance>();
        List<LeaveRequest> leaveRequests = new ArrayList<LeaveRequest>();
        List<LeaveBalance> leaveBalanceList = new ArrayList<LeaveBalance>();

        Employee employeeByConstructor = new Employee(empId, employeeCode, firstName, lastName, contact, emergencyContacts, addresses, position, joinDate, gender, adhaarNumber, panNumber, uanNumber, profileImage, dateOfBirth, salary, department, performanceList, account, leaveRequests, leaveBalanceList);

        assertEquals(empId, employeeByConstructor.getEmployeeId());
        assertEquals(firstName, employeeByConstructor.getFirstName());
        assertEquals(lastName, employeeByConstructor.getLastName());
        assertEquals(contact, employeeByConstructor.getContact());
        assertEquals(position, employeeByConstructor.getPosition());
        assertEquals(joinDate, employeeByConstructor.getJoinDate());
        assertEquals(gender, employeeByConstructor.getGender());
        assertEquals(dateOfBirth, employeeByConstructor.getDateOfBirth());
        assertEquals(salary, employeeByConstructor.getSalary());
        assertEquals(account, employeeByConstructor.getAccount());
        assertEquals(profileImage, employeeByConstructor.getProfileImage());
    }

    @Test
    @DisplayName("test_Employee_setters_and_getters")
    void test_Employee_setters_and_getters(){
        Long empId = 1L;
        String firstName = "test";
        String lastName = "user";
        String contact = "8539228375";
        BigDecimal salary = new BigDecimal(20000);
        Position position = Position.TESTER;
        Gender gender = Gender.MALE;
        String joinDate = "2022/12/12";
        Account account = new Account();
        String dateOfBirth = "2004/12/22";
        List<Address> addresses = new ArrayList<Address>();

        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setAddresses(addresses);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setAccount(account);
        employee.setDateOfBirth(dateOfBirth);

        assertEquals(empId, employee.getEmployeeId());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(contact, employee.getContact());
        assertEquals(addresses, employee.getAddresses());
        assertEquals(salary, employee.getSalary());
        assertEquals(position, employee.getPosition());
        assertEquals(gender, employee.getGender());
        assertEquals(joinDate, employee.getJoinDate());
        assertEquals(account, employee.getAccount());
        assertEquals(dateOfBirth, employee.getDateOfBirth());
    }
}
