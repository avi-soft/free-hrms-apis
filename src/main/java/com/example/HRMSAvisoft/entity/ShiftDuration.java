package com.example.HRMSAvisoft.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Duration;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long shiftDurationId;

    Duration shiftDuration;
}
