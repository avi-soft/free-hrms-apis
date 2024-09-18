package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.ShiftDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftDurationRepository extends JpaRepository<ShiftDuration, Long> {

}
