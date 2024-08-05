package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HRMSAvisoft.entity.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
//    @Query("SELECT d FROM Department d WHERE d.organization.organizationId = :organizationId")
//    List<Department> findAllByOrganizationId(@Param("organizationId") Long organizationId);

//    Optional<Department> findByDepartment(String department);
//
//    @Query("SELECT d FROM Department d WHERE d.department = :department AND d.organization.organizationId = :organizationId")
//    Optional<Department> findByDepartmentAndOrganization(@Param("department") String department, @Param("organizationId") Long organizationId);
}
