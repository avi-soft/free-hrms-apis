package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee getByEmployeeId(Long employeeId);
    // query returns employees searched on name
    @Query("SELECT e FROM Employee e WHERE CONCAT(e.firstName, ' ', e.lastName) LIKE %:name%")
    List<Employee> searchEmployeesByName(@Param("name") String name);

    Employee findTopByOrderByEmployeeCodeDesc();

    boolean existsByEmployeeCode(String employeeCode);

    List<Employee> findAllByDesignationsContaining(Designation designation);

    List<Employee> findAllBySkillsContaining(Skill skill);

    @Query("SELECT e FROM Employee e WHERE e.departments IS EMPTY")
    List<Employee> findAllEmployeeWhereDepartmentsIsEmpty();
}
