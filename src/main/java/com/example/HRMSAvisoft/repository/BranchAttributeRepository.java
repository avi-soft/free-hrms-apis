package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchAttributeRepository extends JpaRepository<BranchAttribute, Long> {
    Optional<BranchAttribute> findByAttributeKey(String attributeKey);

}
