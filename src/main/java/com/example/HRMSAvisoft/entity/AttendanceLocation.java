package com.example.HRMSAvisoft.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@JsonFormat
@Builder
public class AttendanceLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long attendanceLocationId;

    private String attendanceLocation;
    private double latitude;
    private double longitude;
    private double elevation;
}
