package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Gender;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonalDetailsDTO {
    private String firstName;
    private String lastName;
//    private String contact;
//    private Gender gender;
//    private String dateOfBirth;
}
