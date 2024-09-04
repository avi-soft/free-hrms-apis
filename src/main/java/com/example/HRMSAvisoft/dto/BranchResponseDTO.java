package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Organization;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AttributesSerializer;

import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BranchResponseDTO {

    private Long branchId;

    private String branchName;

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<BranchAttribute, String> attributes;

    Set<Organization> organizations;
    Set<Department> departments;

}
