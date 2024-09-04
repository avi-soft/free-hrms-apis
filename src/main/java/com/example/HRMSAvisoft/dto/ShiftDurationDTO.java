package com.example.HRMSAvisoft.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDurationDTO {

    private Integer shiftDurationHours;

    private Integer shiftDurationMinutes;
}
