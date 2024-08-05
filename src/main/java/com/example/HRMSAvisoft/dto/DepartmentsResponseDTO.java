package com.example.HRMSAvisoft.dto;


import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AttributesSerializer;

import java.util.HashMap;
import java.util.Map;

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

    private Long organizationId;

    private String organizationName;

    private String organizationImage;

    private String organizationDescription;


    @JsonSerialize(using = AttributesSerializer.class)
    private Map<DepartmentAttribute, String> attributes = new HashMap<>();
}
