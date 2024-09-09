package com.example.HRMSAvisoft.dto;


import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Organization;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AttributesSerializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private Set<Organization> organizations = new HashSet<>();

    private Set<Branch> branches = new HashSet<>();

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<DepartmentAttribute, String> attributes = new HashMap<>();
}
