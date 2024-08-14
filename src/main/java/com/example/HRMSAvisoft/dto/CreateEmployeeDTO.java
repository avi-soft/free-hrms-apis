package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.entity.Gender;
import com.example.HRMSAvisoft.entity.Position;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AttributesSerializer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDTO {
    private String firstName;
    private String lastName;
    private Long departmentId;
    private String employeeCode;
//    private String contact;
//    private Position position;
//    private String joinDate;
//    private Gender gender;
//    private String adhaarNumber;
//    private String panNumber;
//    private String uanNumber;
//    private String dateOfBirth;
//    private BigDecimal salary;
    private List<String> designationList;
    private List<String> skillList;

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<String, String> attributes = new HashMap<>();
}


