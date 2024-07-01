package com.example.HRMSAvisoft.repository;


import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User getByEmail(String email);
    User getByUserId(Long id);
    User findByEmployee(Employee employee);
}
