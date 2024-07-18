package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
