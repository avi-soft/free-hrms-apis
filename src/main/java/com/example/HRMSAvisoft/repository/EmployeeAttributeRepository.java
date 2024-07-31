package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.EmployeeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmployeeAttributeRepository extends JpaRepository<EmployeeAttribute, Long> {
    Optional<EmployeeAttribute> findByAttributeKey(String attributeKey);
}
