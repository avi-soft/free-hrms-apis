package com.example.HRMSAvisoft.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentsResponseDTO {

    private Long departmentId;

    private String department;

    private String description;

    private Long managerId;

    private String managerEmployeeCode;

    private String managerFirstName;

    private String managerLastName;

}
