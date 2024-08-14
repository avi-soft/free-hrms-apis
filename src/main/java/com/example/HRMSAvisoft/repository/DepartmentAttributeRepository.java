package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentAttributeRepository extends JpaRepository<DepartmentAttribute, Long> {
    Optional<DepartmentAttribute> findByAttributeKey(String attributeKey);

}
