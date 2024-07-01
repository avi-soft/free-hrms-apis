package com.example.HRMSAvisoft.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddAccountDTO {
    @NotBlank(message = "Bank Name is required.")
    private String bankName;
    @NotBlank(message = "Account number is required.")
    @Pattern(regexp = "\\d{9,18}", message = "Bank account number must be between 9 and 18 digits.")
    @Pattern(regexp = "^[0-9]*$", message = "Bank account number must only contain digits.")
    private String accountNumber;
    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Za-z0-9]{6}$", message = "Invalid IFSC code format.")
    private String ifsc;
    @NotBlank(message = "Branch is required !")
    private String branch;
}
