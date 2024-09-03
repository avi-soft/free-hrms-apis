package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.AttendanceLocation;
import com.example.HRMSAvisoft.service.AttendanceLocationService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceLocationRepository extends JpaRepository<AttendanceLocation, Long> {
}
