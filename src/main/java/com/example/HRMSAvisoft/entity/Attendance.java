package com.example.HRMSAvisoft.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@JsonFormat
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private Long userId;
    private LocalDate date;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private Double latitude;
    private Double longitude;
    private Double elevation;
    private Boolean attendanceMarked = false;
}
