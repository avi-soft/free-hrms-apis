package com.example.HRMSAvisoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HRMSAvisoft.entity.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT d FROM Department d WHERE d.organization.organizationId = :organizationId")
    List<Department> findAllByOrganizationId(@Param("organizationId") Long organizationId);

    Optional<Department> findByDepartment(String department);
}
