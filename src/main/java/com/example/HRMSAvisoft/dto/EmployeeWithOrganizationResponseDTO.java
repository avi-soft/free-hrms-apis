package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWithOrganizationResponseDTO {

        private Employee employee;
        private Set<Organization> organizations;
}
