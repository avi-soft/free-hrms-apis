package com.example.HRMSAvisoft.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDTO {
    private Long employeeId;
    private Long userId;
    private String employeeCode;
    private String email;
    private String employeeName;
    private String profileImage;
}
