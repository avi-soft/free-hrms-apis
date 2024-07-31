package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Account;
import com.example.HRMSAvisoft.entity.EmployeeAttribute;
import com.example.HRMSAvisoft.entity.Gender;
import com.example.HRMSAvisoft.entity.Position;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.EmployeeAttributesSerializer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDetailsDTO {

    private Long employeeId;
    private String firstName;
    private String lastName;
//    private String contact;
//    private Position position;
//    private String adhaarNumber;
//    private String panNumber;
//    private String uanNumber;
//    private String joinDate;
//    private Gender gender;
//    private String dateOfBirth;
//    private long salary;

}
