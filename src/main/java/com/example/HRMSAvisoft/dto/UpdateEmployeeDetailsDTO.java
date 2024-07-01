package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Account;
import com.example.HRMSAvisoft.entity.Gender;
import com.example.HRMSAvisoft.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDetailsDTO {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String contact;
    private Position position;
    private String adhaarNumber;
    private String panNumber;
    private String uanNumber;
    private String joinDate;
    private Gender gender;
    private String dateOfBirth;
    private long salary;

}
