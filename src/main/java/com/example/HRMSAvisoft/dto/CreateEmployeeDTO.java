package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.entity.Gender;
import com.example.HRMSAvisoft.entity.Position;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "First name cannot be null")
    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 2, message = "First name must be at least 2 characters long")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "FirstName can only contain letters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, message = "Last name must be at least 2 characters long")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "LastName can only contain letters")
    private String lastName;

    private Long departmentId;

    private String employeeCode;

    private Gender gender;
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


