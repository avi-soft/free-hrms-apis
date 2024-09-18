package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

    Optional<Designation> findByDesignation(String designation);
}
