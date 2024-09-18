package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchAttributeRepository extends JpaRepository<BranchAttribute, Long> {
    Optional<BranchAttribute> findByAttributeKey(String attributeKey);

}
