package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Branch;
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
    Optional<Department> findByDepartment(String department);

    @Query("SELECT d FROM Department d JOIN d.organizations o WHERE d.department = :department AND o.organizationId = :organizationId")
    Optional<Department> findByDepartmentAndOrganizationId(@Param("department") String department, @Param("organizationId") Long organizationId);

    @Query("SELECT d FROM Department d WHERE d.branches IS EMPTY")
    List<Department> findAllDepartmentWhereBranchIsEmpty();

    @Query("SELECT d FROM Department d WHERE d.organizations IS EMPTY")
    List<Department> findAllDepartmentWhereOrganizationIsEmpty();

    @Query("SELECT d FROM Department d JOIN d.branches b WHERE d.department = :department AND b.branchId = :branchId")
    Optional<Department> findByDepartmentAndBranchId(@Param("department") String department, @Param("branchId") Long branchId);

}
