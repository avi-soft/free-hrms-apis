package com.example.HRMSAvisoft.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDTO {

    private Long userId;
    private Double latitude;
    private Double longitude;
    private Double elevation;


}
