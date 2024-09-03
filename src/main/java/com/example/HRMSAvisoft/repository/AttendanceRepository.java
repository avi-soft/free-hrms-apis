package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserIdAndClockOutTimeIsNull(Long userId);

    Optional<Attendance> findByUserIdAndDate(Long userId, LocalDate date);
}
