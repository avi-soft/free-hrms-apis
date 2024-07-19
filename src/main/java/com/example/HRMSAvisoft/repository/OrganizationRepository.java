package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> getByOrganizationName(String organizationName);
}
