package com.example.HRMSAvisoft.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserDTO {
    private String email;
    private String password;
}
