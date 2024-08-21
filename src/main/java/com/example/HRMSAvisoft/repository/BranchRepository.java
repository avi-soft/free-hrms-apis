package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findBranchByName(String branchName);

    @Query("SELECT b FROM Branch b JOIN b.organizations o WHERE b.branch = :branch AND o.organizationId = :organizationId")
    Optional<Branch> findByBranchAndOrganizationId(@Param("branch") String branch, @Param("organizationId") Long organizationId);
}
