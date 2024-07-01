package com.example.HRMSAvisoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HRMSAvisoft.entity.Department;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
