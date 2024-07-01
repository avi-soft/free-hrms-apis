package com.example.HRMSAvisoft.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddNewUserDTO {
    @NotBlank(message = "Invalid Email : Email field is empty !")
    @Email(message = "Invalid email !")
    private String email;
    @NotBlank(message="Invalid Password : Password field is empty !")
    private String password;
    @NotBlank(message = "Invalid Role : Role field is empty !")
    private String role;
}
