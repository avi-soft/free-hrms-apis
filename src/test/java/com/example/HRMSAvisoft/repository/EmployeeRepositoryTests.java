package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;


    Long empId = 9L;
    String firstName = "newUser";
    String lastName = "testeruser";
    String contact = "8539228375";
    BigDecimal salary = new BigDecimal(20000);
    Position position = Position.TESTER;
    Gender gender = Gender.MALE;
    String joinDate = "2022/12/12";
    String dateOfBirth = "2004/12/22";

    Employee savedEmployee;


    @Test
    @DisplayName("test_save_employee")
    @Transactional
    void testSaveEmployee(){

        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setDateOfBirth(dateOfBirth);

        savedEmployee = employeeRepository.save(employee);

        assertEquals(empId, savedEmployee.getEmployeeId());
        assertEquals(firstName, savedEmployee.getFirstName());
        assertEquals(lastName, savedEmployee.getLastName());
        assertEquals(contact, savedEmployee.getContact());
        assertEquals(salary, savedEmployee.getSalary());
        assertEquals(position, savedEmployee.getPosition());
        assertEquals(gender, savedEmployee.getGender());
        assertEquals(joinDate, savedEmployee.getJoinDate());
        assertEquals(dateOfBirth, savedEmployee.getDateOfBirth());
    }

    @Test
    @DisplayName("test_find_employee_by_id")
    @Transactional
    void testfindEmployeeById(){

        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setDateOfBirth(dateOfBirth);

        savedEmployee = employeeRepository.save(employee);

        Employee employeeFoundById = employeeRepository.findById(empId).orElse(null);

        assertEquals(employeeFoundById.getEmployeeId(), savedEmployee.getEmployeeId());
        assertEquals(employeeFoundById.getFirstName(), savedEmployee.getFirstName());
        assertEquals(employeeFoundById.getLastName(), savedEmployee.getLastName());
        assertEquals(employeeFoundById.getContact(), savedEmployee.getContact());
        assertEquals(employeeFoundById.getSalary(), savedEmployee.getSalary());
        assertEquals(employeeFoundById.getPosition(), savedEmployee.getPosition());
        assertEquals(employeeFoundById.getGender(), savedEmployee.getGender());
        assertEquals(employeeFoundById.getJoinDate(), savedEmployee.getJoinDate());
        assertEquals(employeeFoundById.getDateOfBirth(), savedEmployee.getDateOfBirth());
    }

    @Test
    @DisplayName("test_find_All_Employees")
    @Transactional
    void testfindAllEmployees(){
        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setDateOfBirth(dateOfBirth);

        savedEmployee = employeeRepository.save(employee);

        List<Employee> employees = employeeRepository.findAll();

        assertEquals(1, employees.size());
        assertEquals(empId, employees.get(0).getEmployeeId());
        assertEquals(firstName, employees.get(0).getFirstName());
    }

    @Test
    @DisplayName("test_delete_employee")
    @Transactional
    void testDeleteEmployee(){
        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setDateOfBirth(dateOfBirth);

        savedEmployee = employeeRepository.save(employee);

        employeeRepository.delete(savedEmployee);

        Employee findEmployeeAfterDeletion = employeeRepository.findById(empId).orElse(null);

        assertNull(findEmployeeAfterDeletion);

    }

    @Test
    @DisplayName("test_update_employee")
    @Transactional
    void testUpdateEmployee(){
        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setDateOfBirth(dateOfBirth);

        savedEmployee = employeeRepository.save(employee);

        Employee employeefoundById = employeeRepository.findById(empId).orElse(null);

        employeefoundById.setFirstName("John");
        employeefoundById.setLastName("Cena");

        Employee updatedEmployee = employeeRepository.save(employeefoundById);

        assertNotNull(updatedEmployee);
        assertEquals("John", updatedEmployee.getFirstName());
        assertEquals("Cena", updatedEmployee.getLastName());
    }

    @Test
    @DisplayName("test_searchEmployeesByName")
    @Transactional
    void test_searchEmployeesByName(){
        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setDateOfBirth(dateOfBirth);

        savedEmployee = employeeRepository.save(employee);

        List<Employee> employeeFoundBySearch = employeeRepository.searchEmployeesByName(firstName);
        assertEquals(1, employeeFoundBySearch.size());
        assertEquals(empId, employeeFoundBySearch.get(0).getEmployeeId());
        assertEquals(firstName, employeeFoundBySearch.get(0).getFirstName());
        assertEquals(dateOfBirth, employeeFoundBySearch.get(0).getDateOfBirth());
    }


}
