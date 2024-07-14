package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> getByRole(String role);
}
