package com.example.HRMSAvisoft.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmergencyContactDTO {
    @NotBlank(message = "Invalid Contact : Contact field is empty")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",message = "Invalid Format for phone Number")
    private String contact;

    @NotBlank(message = "Relationship field is empty")
    @Pattern(regexp = "^[a-zA-Z\\s-]+$",message = "Only Alphabets (A-Z) are allowed")
    private String relationship;
}
