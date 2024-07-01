package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonalDetailsDTO {
    private String firstName;
    private String lastName;
    private String contact;
    private Gender gender;
    private String dateOfBirth;
}
