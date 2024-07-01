package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllPerformanceOfEmployeeDTO {
    private Long performanceId;

    private String firstName;

    private String lastName;

    private String employeeCode;

    private String reviewDate;

    private Rating rating;

    private String comment;

    private String reviewerFirstName;

    private String reviewerLastName;

    private String reviewerEmployeeCode;
}
