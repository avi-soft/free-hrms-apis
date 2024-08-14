package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.attribute.OrganizationAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OrganizationAttributeRepository extends JpaRepository<OrganizationAttribute, Long>
{
    Optional<OrganizationAttribute> findByAttributeKey(String attributeKey);
}
