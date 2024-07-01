package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Role getByRole(String role);
}
