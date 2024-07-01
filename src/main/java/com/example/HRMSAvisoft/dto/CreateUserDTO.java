package com.example.HRMSAvisoft.dto;

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
public class CreateUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String joinDate;
    private String role;
    private Position position;
    private String dateOfBirth;
    private Gender gender;
    private BigDecimal salary;
}
