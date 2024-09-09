package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserIdAndClockOutTimeIsNull(Long userId);

    Optional<Attendance> findByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.userId = :userId AND FUNCTION('MONTH', a.date) = :month")
    List<Attendance> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") int month);
}
